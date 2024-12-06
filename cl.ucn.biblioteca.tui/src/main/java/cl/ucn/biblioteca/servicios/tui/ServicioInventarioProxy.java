package cl.ucn.biblioteca.servicios.tui;

import cl.ucn.biblioteca.api.Libro;
import cl.ucn.biblioteca.servicio.api.ServicioInventarioLibro;

import org.apache.felix.service.command.Descriptor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import cl.ucn.biblioteca.api.ExcepcionLibroExistente;
import cl.ucn.biblioteca.api.ExcepcionLibroInvalido;


public class ServicioInventarioProxy {

	public static final String AMBITO = "libro";
	public static final String[] FUNCIONES = new String[] {"ingresar", "remover", "modificarCategoria", "obtener"};
	private BundleContext contexto;
	public ServicioInventarioProxy(BundleContext contexto)
	{
		this.contexto = contexto;
	}

	@Descriptor("Ingresar libro")
	public String ingresar(@Descriptor("ISBN") String isbn,
						   @Descriptor("Titulo") String titulo,
						   @Descriptor("Autor") String autor,
						   @Descriptor("Categoria") String categoria)
			throws ExcepcionLibroExistente, ExcepcionLibroInvalido
	{
		ServicioInventarioLibro servicio = obtenerServicio();
		servicio.ingresar(isbn, titulo, autor, categoria);
		return isbn;
	}


	@Descriptor("Remover libro")
	public String remover(@Descriptor("ISBN") String isbn) {
		try {
			ServicioInventarioLibro servicio = obtenerServicio();
			servicio.remover(isbn);
			return "Libro con ISBN " + isbn + " ha sido removido exitosamente.";
		} catch (Exception e) {
			return "Error al remover el libro: " + e.getMessage();
		}
	}


	@Descriptor("Modificar la categoría de un libro")
	public String modificarCategoria(@Descriptor("ISBN") String isbn,
									 @Descriptor("Nueva Categoría") String categoria) {
		try {
			ServicioInventarioLibro servicio = obtenerServicio();
			servicio.modificarCategoria(isbn, categoria);
			return "Categoría del libro con ISBN " + isbn + " actualizada a: " + categoria;
		} catch (Exception e) {
			return "Error al modificar la categoría: " + e.getMessage();
		}
	}

	@Descriptor("Obtener información de un libro")
	public String obtener(@Descriptor("ISBN") String isbn) {
		try {
			ServicioInventarioLibro servicio = obtenerServicio();
			Libro libro = servicio.obtener(isbn);
			return "Información del libro:\n" +
					"ISBN: " + libro.getIsbn() + "\n" +
					"Título: " + libro.getTitulo() + "\n" +
					"Autor: " + libro.getAutor() + "\n" +
					"Categoría: " + libro.getCategoria();
		} catch (Exception e) {
			return "Error al obtener el libro: " + e.getMessage();
		}
	}




	protected ServicioInventarioLibro obtenerServicio() {

		ServiceReference referencia = contexto.getServiceReference(ServicioInventarioLibro.class.getName());
		if (referencia == null)
			throw new RuntimeException("ServicioInventarioLibro no esta registrado, no puedo invocar operacion");

		ServicioInventarioLibro servicio = (ServicioInventarioLibro) this.contexto.getService(referencia);
		if (servicio == null)
			throw new RuntimeException("ServicioInventarioLibro no esta registrado, no puedo invocar operacion");

		return servicio;
	}

}
