package es.udc.asi.restexample.model.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.asi.restexample.model.domain.Candidatura;
import es.udc.asi.restexample.model.domain.Categoria;
import es.udc.asi.restexample.model.domain.Eleccion;
import es.udc.asi.restexample.model.domain.Grupos;
import es.udc.asi.restexample.model.domain.Titulacion;
import es.udc.asi.restexample.model.domain.Usuario;
import es.udc.asi.restexample.model.exception.AlreadyParticipatedException;
import es.udc.asi.restexample.model.exception.DeadlineException;
import es.udc.asi.restexample.model.exception.InvalidGroupException;
import es.udc.asi.restexample.model.exception.NotFoundException;
import es.udc.asi.restexample.model.repository.CandidaturaDao;
import es.udc.asi.restexample.model.repository.EleccionDao;
import es.udc.asi.restexample.model.repository.UsuarioDao;
import es.udc.asi.restexample.model.service.dto.CandidaturaDTO;
import es.udc.asi.restexample.model.service.dto.UsuarioDTOPublico;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class CandidaturaService {

	@Autowired
	private CandidaturaDao candidaturaDAO;
	
	@Autowired
	private UsuarioDao usuarioDAO;
	
	@Autowired
	private EleccionDao eleccionDAO;
	
	public List<CandidaturaDTO> findAll() {
	  return candidaturaDAO.findAll().stream().map(candidatura -> new CandidaturaDTO(candidatura)).collect(Collectors.toList());
	}
	
	public CandidaturaDTO findById(Long id) throws NotFoundException {
	  Candidatura candidatura = candidaturaDAO.findById(id);
	  if (candidatura == null) {
	    throw new NotFoundException(id.toString(), Eleccion.class);
	  }
	  return new CandidaturaDTO(candidaturaDAO.findById(id));
	}
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public CandidaturaDTO create(CandidaturaDTO candidatura) throws DeadlineException, InvalidGroupException, AlreadyParticipatedException {
	  Eleccion bdEleccion = eleccionDAO.findById(candidatura.getEleccion().getId());
	  Usuario candidato = usuarioDAO.findById(candidatura.getCandidato().getId());
	  if (!comprobarPermiso(bdEleccion, candidato, false)) {
		  throw new InvalidGroupException();
	  }
	  yaCandidato(bdEleccion, candidato);
	  if (bdEleccion.getFechaLimitePresentacion().isBefore(LocalDate.now())) {
		  throw new DeadlineException(candidatura.getId().toString(), Candidatura.class, "presentar");
	  } else {
		  Candidatura bdCandidatura = new Candidatura(candidatura.getMotivo(), bdEleccion, candidato);
		  candidaturaDAO.create(bdCandidatura);
		  return new CandidaturaDTO(bdCandidatura);
	  }
	}
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void deleteById(Long id) throws DeadlineException {
	  Candidatura candidatura = candidaturaDAO.findById(id);
	  Eleccion bdEleccion = candidatura.getEleccion();
	  if (bdEleccion.getFechaLimitePresentacion().isBefore(LocalDate.now())) {
		  throw new DeadlineException(candidatura.getId().toString(), Candidatura.class, "cancelar");
	  } else {
		  candidaturaDAO.deleteById(candidatura.getId());
	  }
	}
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public CandidaturaDTO update(CandidaturaDTO candidatura) throws DeadlineException {
	  Candidatura bdCandidatura = candidaturaDAO.findById(candidatura.getId());
	  bdCandidatura.setMotivo(candidatura.getMotivo()); //solo puede modificar motivos, ni eleccion ni candidato
	  Eleccion bdEleccion = eleccionDAO.findById(candidatura.getEleccion().getId());
	  if (bdEleccion.getFechaLimitePresentacion().isBefore(LocalDate.now())) {
		  throw new DeadlineException(candidatura.getId().toString(), Candidatura.class, "editar");
	  } else {
		  candidaturaDAO.update(bdCandidatura);
		  return new CandidaturaDTO(bdCandidatura);
	  }
	}
	

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public CandidaturaDTO votar(Long idCand, UsuarioDTOPublico usuario) throws DeadlineException, InvalidGroupException, AlreadyParticipatedException {
		Candidatura bdCandidatura = candidaturaDAO.findById(idCand);
		Usuario bdUsuario = usuarioDAO.findById(usuario.getId());
		Eleccion bdEleccion = eleccionDAO.findById(bdCandidatura.getEleccion().getId());
		comprobarPermiso(bdEleccion, bdUsuario, true);
		yaVotante(bdEleccion, bdUsuario);
		if (bdEleccion.getFechaLimiteVotacion().isBefore(LocalDate.now()) || (!LocalDate.now().isAfter(bdEleccion.getFechaLimitePresentacion()))) {
			throw new DeadlineException(bdCandidatura.getId().toString(), Candidatura.class, "votar");
		} else {
			Set<Usuario> listaVotantes = bdCandidatura.getVotantes();
			listaVotantes.add(bdUsuario);
			bdCandidatura.setVotantes(listaVotantes);
			bdCandidatura.setNumVotos(bdCandidatura.getVotantes().size());
			candidaturaDAO.update(bdCandidatura);
			
			List<Candidatura> listaVotos = new ArrayList<>();
			listaVotos = bdUsuario.getVotos();
			listaVotos.add(bdCandidatura);
			bdUsuario.setVotos(listaVotos);
			usuarioDAO.update(bdUsuario);
		}
		return new CandidaturaDTO(bdCandidatura);
	}
	
	public boolean comprobarPermiso (Eleccion eleccion, Usuario usuario, boolean votantes) throws InvalidGroupException {
		Grupos grupoEle;
		boolean permitido = false;
		if (votantes) {
			grupoEle = eleccion.getGrupoVotantes();
		} else {
			grupoEle = eleccion.getGrupoCandidatos();
		}
		if (!grupoEle.equals(Grupos.Todos)) {
			Categoria catUsu = usuario.getCategoria();
			if ((grupoEle.equals(Grupos.Alumnado) && !catUsu.equals(Categoria.Alumnado)) || (grupoEle.equals(Grupos.Profesorado) && !catUsu.equals(Categoria.Profesorado))) {
				return false;
			}
		}
		Set<Titulacion> titusEle = eleccion.getTitulaciones();
		List<Titulacion> titusUsu = usuario.getTitulaciones();
		permitido = titusEle.stream().anyMatch(tit -> { 
			return titusUsu.contains(tit);
		});
		if (!permitido) {
			return false;
		};
		return true;
	}
	
	public void yaCandidato (Eleccion eleccion, Usuario usuario) throws AlreadyParticipatedException {
		List<Candidatura> candidaturas = candidaturaDAO.findAll();
		boolean candidato = candidaturas.stream().anyMatch(cand -> {
			if (cand.getEleccion().equals(eleccion)) {
				return cand.getCandidato().equals(usuario);
			}
			return false;
		});
		if (candidato) {
			throw new AlreadyParticipatedException("te has presentado", eleccion.getId().toString(), eleccion.getClass());
		} else {
			return;
		}
	}
	
	public void yaVotante (Eleccion eleccion, Usuario usuario) throws AlreadyParticipatedException {
		List<Candidatura> candidaturas = candidaturaDAO.findAll();
		boolean votado = candidaturas.stream().anyMatch(cand -> {
			if (cand.getEleccion().equals(eleccion)) {
				return cand.getVotantes().stream().anyMatch(votante -> {
					return votante.equals(usuario);
				});
			}
			return false;
		});
		if (votado) {
			throw new AlreadyParticipatedException("has votado", eleccion.getId().toString(), Eleccion.class);
		} else {
			return;
		}
	}
}
