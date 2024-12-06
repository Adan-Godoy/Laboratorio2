package cl.ucn.biblioteca.servicio.api;

import java.util.Set;

import cl.ucn.biblioteca.api.LibroMutable;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import cl.ucn.biblioteca.api.ExcepcionLibroNoEncontrado;
import cl.ucn.biblioteca.api.Inventario;
import cl.ucn.biblioteca.api.Libro;

public class ServicioInventarioLibroImpl implements ServicioInventarioLibro{


	private BundleContext contexto;

	public ServicioInventarioLibroImpl(BundleContext contexto) {
		
		this.contexto = contexto;
	}

	@Override
	public void ingresar(String isbn, String titulo, String autor, String categoria) {
		try {
			Inventario inventario = buscarLibroEnInventario();

			LibroMutable libro = inventario.crearLibro(isbn);

			libro.setTitulo(titulo);
			libro.setAutor(autor);
			libro.setCategoria(categoria);

			inventario.guardarLibro(libro);
			System.out.println("Libro ingresado exitosamente: " + isbn);
		} catch (Exception e) {
			System.err.println("Error al ingresar el libro: " + e.getMessage());
		}
	}

	@Override
	public void remover(String isbn) {
		try {
			Inventario inventario = buscarLibroEnInventario();
			inventario.removerLibro(isbn);
			System.out.println("Libro removido exitosamente: " + isbn);
		} catch (Exception e) {
			System.err.println("Error al remover el libro: " + e.getMessage());
		}
	}

	@Override
	public void modificarCategoria(String isbn, String categoria) {
		try {
			Inventario inventario = buscarLibroEnInventario();
			LibroMutable libro = inventario.cargarLibroParaEdicion(isbn);

			libro.setCategoria(categoria);
			
			inventario.guardarLibro(libro);
			System.out.println("Categoría modificada exitosamente para el libro: " + isbn);
		} catch (Exception e) {
			System.err.println("Error al modificar la categoría: " + e.getMessage());
		}
	}


	@Override
	public Libro obtener(String isbn) throws ExcepcionLibroNoEncontrado {
		Inventario inventario = buscarLibroEnInventario();
		return inventario.cargarLibro(isbn);
	}


	private Inventario buscarLibroEnInventario()  {
		
		String nombre = Inventario.class.getName();
		ServiceReference ref = this.contexto.getServiceReference(nombre);
		return (Inventario) this.contexto.getService(ref);
	}
}
