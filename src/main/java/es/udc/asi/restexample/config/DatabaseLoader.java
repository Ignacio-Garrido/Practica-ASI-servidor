package es.udc.asi.restexample.config;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import es.udc.asi.restexample.model.domain.Candidatura;
import es.udc.asi.restexample.model.domain.Categoria;
import es.udc.asi.restexample.model.domain.Eleccion;
import es.udc.asi.restexample.model.domain.Grupos;
import es.udc.asi.restexample.model.domain.Titulacion;
import es.udc.asi.restexample.model.domain.Usuario;
import es.udc.asi.restexample.model.exception.UserCorreoExistsException;
import es.udc.asi.restexample.model.repository.CandidaturaDao;
import es.udc.asi.restexample.model.repository.EleccionDao;
import es.udc.asi.restexample.model.repository.TitulacionDao;
import es.udc.asi.restexample.model.repository.UsuarioDao;
import es.udc.asi.restexample.model.service.UsuarioService;

@Configuration
public class DatabaseLoader {
  private final Logger logger = LoggerFactory.getLogger(DatabaseLoader.class);

  @Autowired
  private PasswordEncoder passwordEncoder;
  
  @Autowired
  private TitulacionDao titulacionDAO;
  
  @Autowired
  private EleccionDao eleccionDAO;
  
  @Autowired
  private UsuarioDao usuarioDAO;
  
  @Autowired
  private CandidaturaDao candidaturaDAO;
  
  @Autowired
  private DatabaseLoader databaseLoader;

  /*
   * Para hacer que la carga de datos sea transacional, hay que cargar el propio
   * objeto como un bean y lanzar el método una vez cargado, ya que en el
   * PostConstruct (ni similares) se tienen en cuenta las anotaciones de
   * transaciones.
   */
  @PostConstruct
  public void init() {
    try {
      databaseLoader.loadData();
    } catch (UserCorreoExistsException e) {
      logger.error(e.getMessage(), e);
    }
  }

  @Transactional(readOnly = false, rollbackFor = Exception.class)
  public void loadData() throws UserCorreoExistsException {
	// Creamos titulaciones
	Titulacion ed_inf = new Titulacion("Educación Infantil"); //titulacion1
	Titulacion ed_pri = new Titulacion("Educación Primaria"); //titulacion2
	Titulacion der = new Titulacion("Derecho"); //titulacion3

	titulacionDAO.create(ed_inf);
	titulacionDAO.create(ed_pri);
	titulacionDAO.create(der);
	  
	// Creamos usuarios
	Usuario admin = new Usuario("pepe@udc.es", passwordEncoder.encode("pepe"), "Pepe", "Perez", "Lopez", Categoria.Admin);
	usuarioDAO.create(admin); //admin
	Usuario profe1 = new Usuario("juan.ramon@udc.es", passwordEncoder.encode("juan.ramon"), "Juan Ramon", "Armando", "Fernandez", Categoria.Profesorado);
    profe1.getTitulaciones().add(der);
    profe1.getTitulaciones().add(ed_pri);
    usuarioDAO.create(profe1); //profe1
	Usuario profe2 = new Usuario("luis.dominguez@udc.es", passwordEncoder.encode("luis.dominguez"), "Luis", "Dominguez", "Dominguez", Categoria.Profesorado);
    profe2.getTitulaciones().add(der);
	usuarioDAO.create(profe2); //profe2
	Usuario profe3 = new Usuario("gimena.lopez@udc.es", passwordEncoder.encode("gimena.lopez"), "Gimena", "Lopez", "Suarez", Categoria.Profesorado);
    profe3.getTitulaciones().add(ed_inf);
    profe3.getTitulaciones().add(ed_pri);
	usuarioDAO.create(profe3); //profe3
	Usuario alumno1 = new Usuario("lucia.matamoros@udc.es", passwordEncoder.encode("lucia.matamoros"), "Lucia", "Matamoros", "Gonzalez", Categoria.Alumnado);
    alumno1.getTitulaciones().add(ed_pri);
    alumno1.getTitulaciones().add(ed_inf);
	usuarioDAO.create(alumno1); //alumno1
	Usuario alumno2 = new Usuario("pedro.angulo@udc.es", passwordEncoder.encode("pedro.angulo"), "Pedro", "Angulo", "Dominguez", Categoria.Alumnado);
    alumno2.getTitulaciones().add(der);
	usuarioDAO.create(alumno2); //alumno2
	Usuario alumno3 = new Usuario("martin.pio@udc.es", passwordEncoder.encode("martin.pio"), "Martin", "Pio", "Coll", Categoria.Alumnado);
    alumno3.getTitulaciones().add(der);
	usuarioDAO.create(alumno3); //alumno3
	Usuario alumno4 = new Usuario("oscar.martinez@udc.es", passwordEncoder.encode("oscar.martinez"), "Oscar", "Martinez", "Segundo", Categoria.Alumnado);
    alumno4.getTitulaciones().add(der);
    alumno4.getTitulaciones().add(ed_pri);
	usuarioDAO.create(alumno4); //alumno4
	Usuario alumno5 = new Usuario("nuria.fernandez@udc.es", passwordEncoder.encode("nuria.fernandez"), "Nuria", "Fernandez", "Castro", Categoria.Alumnado);
    alumno5.getTitulaciones().add(der);
	usuarioDAO.create(alumno5); //alumno5
      
	// Creamos elecciones
    Set<Titulacion> titusEleEd = new HashSet<>();
    titusEleEd.add(ed_inf);
    titusEleEd.add(ed_pri);
    Eleccion dec_edu_f = new Eleccion("Decanato Educacion 2018", "Facultad de Educacion", Grupos.Profesorado, Grupos.Todos, LocalDate.parse("2018-02-15"), LocalDate.parse("2018-02-17"), titusEleEd);
	eleccionDAO.create(dec_edu_f);  //FinalizadaEleccion1
	Eleccion dec_edu_a = new Eleccion("Decanato Educacion 2022", "Facultad de Educacion", Grupos.Profesorado, Grupos.Todos, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), titusEleEd);
	eleccionDAO.create(dec_edu_a);  //ActivaEleccion1
    Eleccion dec_edu_p = new Eleccion("Decanato Educacion 2026", "Facultad de Educacion", Grupos.Profesorado, Grupos.Todos, LocalDate.parse("2026-04-05"), LocalDate.parse("2026-04-07"), titusEleEd);
	eleccionDAO.create(dec_edu_p);  //ProximaEleccion1
	Set<Titulacion> titusEleDer = new HashSet<>();
    titusEleDer.add(der);
    Eleccion dec_der_f = new Eleccion("Decanato Derecho 2018", "Facultad de Derecho", Grupos.Profesorado, Grupos.Todos, LocalDate.parse("2018-04-20"), LocalDate.parse("2018-04-22"), titusEleDer);
	eleccionDAO.create(dec_der_f); //FinalizadaEleccion2
	Eleccion dec_der_a = new Eleccion("Decanato Derecho 2022", "Facultad de Derecho", Grupos.Profesorado, Grupos.Todos, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), titusEleDer);
	eleccionDAO.create(dec_der_a); //ActivaEleccion2
	Eleccion dec_der_p = new Eleccion("Decanato Derecho 2026", "Facultad de Derecho", Grupos.Profesorado, Grupos.Todos, LocalDate.parse("2026-04-24"), LocalDate.parse("2026-04-26"), titusEleDer);
	eleccionDAO.create(dec_der_p); //ProximaEleccion2
	Eleccion rep_der_f = new Eleccion("Repr. estudiantes de Derecho 2021", "Facultad de Derecho", Grupos.Alumnado, Grupos.Alumnado, LocalDate.parse("2021-04-21"), LocalDate.parse("2021-04-22"), titusEleDer); 
	eleccionDAO.create(rep_der_f); //FinalizadaEleccion3
	Eleccion rep_der_a = new Eleccion("Repr. estudiantes de Derecho 2022", "Facultad de Derecho", Grupos.Alumnado, Grupos.Alumnado, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), titusEleDer); 
	eleccionDAO.create(rep_der_a); //ActivaEleccion3
	Eleccion rep_der_p = new Eleccion("Repr. estudiantes de Derecho 2023", "Facultad de Derecho", Grupos.Alumnado, Grupos.Alumnado, LocalDate.parse("2023-05-13"), LocalDate.parse("2023-05-15"), titusEleDer); 
	eleccionDAO.create(rep_der_p); //ProximaEleccion3
	
	// Creamos candidaturas
	//Finalizadas
	Candidatura f_dec_ed_juan = new Candidatura("Oh Juan Miguel, oh Juan Miguel, todos queremos que marque Juan Miguel!", eleccionDAO.findByNombre("Decanato Educacion 2018"), usuarioDAO.findByCorreo("juan.ramon@udc.es"));
	Candidatura f_dec_ed_gimena = new Candidatura("Esta facultad necesita un cambio radical", eleccionDAO.findByNombre("Decanato Educacion 2018"), usuarioDAO.findByCorreo("gimena.lopez@udc.es"));
	Candidatura f_dec_der_juan = new Candidatura("Ancara Messi", eleccionDAO.findByNombre("Decanato Derecho 2018"), usuarioDAO.findByCorreo("juan.ramon@udc.es"));
	Candidatura f_dec_der_luis = new Candidatura("Ay mi madre El Bichooo!!!", eleccionDAO.findByNombre("Decanato Derecho 2018"), usuarioDAO.findByCorreo("luis.dominguez@udc.es"));
	Candidatura f_rep_der_pedro = new Candidatura("Si me votáis prohíbo los exámenes", eleccionDAO.findByNombre("Repr. estudiantes de Derecho 2021"), usuarioDAO.findByCorreo("pedro.angulo@udc.es"));
	Candidatura f_rep_der_martin = new Candidatura("No he pisado la facultad en mi vida, Hulio", eleccionDAO.findByNombre("Repr. estudiantes de Derecho 2021"), usuarioDAO.findByCorreo("martin.pio@udc.es"));
	// Activas
	Candidatura a_dec_ed_juan = new Candidatura("Me presento a esta elección porque creo que podría conseguir un futuro bonito y próspero para la Facultad de Educación", eleccionDAO.findByNombre("Decanato Educacion 2022"), usuarioDAO.findByCorreo("juan.ramon@udc.es"));
	Candidatura a_dec_ed_gimena = new Candidatura("Aún no lo he decidido", eleccionDAO.findByNombre("Decanato Educacion 2022"), usuarioDAO.findByCorreo("gimena.lopez@udc.es"));
	Candidatura a_dec_der_juan = new Candidatura("Me presento a esta elección porque creo que podría conseguir un futuro bonito y próspero para la Facultad de Derecho", eleccionDAO.findByNombre("Decanato Derecho 2022"), usuarioDAO.findByCorreo("juan.ramon@udc.es"));
	Candidatura a_dec_der_luis = new Candidatura("Me presento a esta elección porque me obliga mi madre", eleccionDAO.findByNombre("Decanato Derecho 2022"), usuarioDAO.findByCorreo("luis.dominguez@udc.es"));
	Candidatura a_rep_der_pedro = new Candidatura("Me presento a esta elección por los loles", eleccionDAO.findByNombre("Repr. estudiantes de Derecho 2022"), usuarioDAO.findByCorreo("pedro.angulo@udc.es"));
	Candidatura a_rep_der_martin = new Candidatura("IDK", eleccionDAO.findByNombre("Repr. estudiantes de Derecho 2022"), usuarioDAO.findByCorreo("martin.pio@udc.es"));
	Candidatura a_rep_der_nuria = new Candidatura("Me presento a esta elección porque quiero crédtios gratis", eleccionDAO.findByNombre("Repr. estudiantes de Derecho 2022"), usuarioDAO.findByCorreo("nuria.fernandez@udc.es"));

	// Añadimos votos
	Set<Usuario> votantes1 = new HashSet<>();
	votantes1.add(alumno4);
	votantes1.add(profe3);
	f_dec_ed_juan.setVotantes(votantes1);
	f_dec_ed_juan.setNumVotos(2);
	Set<Usuario> votantes2 = new HashSet<>();
	votantes2.add(alumno4);
	f_dec_der_luis.setVotantes(votantes2);
	f_dec_der_luis.setNumVotos(1);
	Set<Usuario> votantes3 = new HashSet<>();
	votantes3.add(alumno5);
	votantes3.add(alumno3);
	f_rep_der_pedro.setVotantes(votantes3);
	f_rep_der_pedro.setNumVotos(2);
	Set<Usuario> votantes4 = new HashSet<>();
	votantes4.add(alumno2);
	f_rep_der_martin.setVotantes(votantes4);
	f_rep_der_martin.setNumVotos(1);
	
	candidaturaDAO.create(f_dec_ed_juan);
	candidaturaDAO.create(f_dec_ed_gimena);
	candidaturaDAO.create(f_dec_der_juan);
	candidaturaDAO.create(f_dec_der_luis);
	candidaturaDAO.create(f_rep_der_pedro);
	candidaturaDAO.create(f_rep_der_martin);
	candidaturaDAO.create(a_dec_ed_juan);
	candidaturaDAO.create(a_dec_ed_gimena);
	candidaturaDAO.create(a_dec_der_juan);
	candidaturaDAO.create(a_dec_der_luis);
	candidaturaDAO.create(a_rep_der_pedro);
	candidaturaDAO.create(a_rep_der_martin);
	candidaturaDAO.create(a_rep_der_nuria);
	
  }
}
