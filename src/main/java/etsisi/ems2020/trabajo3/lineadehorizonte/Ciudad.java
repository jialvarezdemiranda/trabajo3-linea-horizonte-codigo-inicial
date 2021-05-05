package etsisi.ems2020.trabajo3.lineadehorizonte;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/*
 Clase fundamental.
 Sirve para hacer la lectura del fichero de entrada que contiene los datos de como
 están situados los edificios en el fichero de entrada. xi, xd, h, siendo. Siendo
 xi la coordenada en X origen del edificio iésimo, xd la coordenada final en X, y h la altura del edificio.
 
 */
public class Ciudad {

	private ArrayList<Edificio> ciudad;

	public Ciudad() {

		/*
		 * Generamos una ciudad de manera aleatoria para hacer pruebas.
		 */
		ciudad = new ArrayList<Edificio>();

		metodoRandom(5);

		ciudad = new ArrayList<Edificio>();
	}

	public Edificio getEdificio(int i) {
		return (Edificio) this.ciudad.get(i);
	}

	public void addEdificio(Edificio e) {
		ciudad.add(e);
	}

	public void removeEdificio(int i) {
		ciudad.remove(i);
	}

	public int size() {
		return ciudad.size();
	}

	public LineaHorizonte getLineaHorizonte() {
		// pi y pd, representan los edificios de la izquierda y de la derecha.
		int pi = 0;
		int pd = ciudad.size() - 1;
		return crearLineaHorizonte(pi, pd);
	}

	public LineaHorizonte crearLineaHorizonte(int pi, int pd) {
		LineaHorizonte linea = new LineaHorizonte(); // LineaHorizonte de salida

// Caso base, la ciudad solo tiene un edificio, el perfil es el de ese edificio. 
		if (pi == pd) {
			crearLineaBase(pi,linea);
		} else {
// Edificio mitad
			int medio = (pi + pd) / 2;

			LineaHorizonte s1 = this.crearLineaHorizonte(pi, medio);
			LineaHorizonte s2 = this.crearLineaHorizonte(medio + 1, pd);
			linea = LineaHorizonteFussion(s1, s2);
		}
		return linea;
	}

	private void crearLineaBase(int pi, LineaHorizonte linea) {
		Punto p1 = new Punto(); // punto donde se guardara en su X la Xi del efificio y en su Y la altura del
								// edificio
		Punto p2 = new Punto(); // punto donde se guardara en su X la Xd del efificio y en su Y le pondremos el
								// valor 0
		Edificio edificio = new Edificio();
		edificio = this.getEdificio(pi); // Obtenemos el único edificio y lo guardo en b
		// En cada punto guardamos la coordenada X y la altura.
		p1.setX(edificio.getXi());
		p1.setY(edificio.getY()); // guardo la altura
		p2.setX(edificio.getXd());
		p2.setY(0); // como el edificio se compone de 3 variables, en la Y de p2 le añadiremos un 0
		// Añado los puntos a la línea del horizonte
		linea.addPunto(p1);
		linea.addPunto(p2);

	}

	/**
	 * Función encargada de fusionar los dos LineaHorizonte obtenidos por la técnica
	 * divide y vencerás. Es una función muy compleja ya que es la encargada de
	 * decidir si un edificio solapa a otro, si hay edificios contiguos, etc. y
	 * solucionar dichos problemas para que el LineaHorizonte calculado sea el
	 * correcto.
	 */
	public LineaHorizonte LineaHorizonteFussion(LineaHorizonte s1, LineaHorizonte s2) {
		// en estas variables guardaremos las alturas de los puntos anteriores, en s1y
		// la del s1, en s2y la del s2
		// y en prev guardaremos la previa del segmento anterior introducido

		int s1y = -1, s2y = -1, prev = -1;
		LineaHorizonte salida = new LineaHorizonte(); // LineaHorizonte de salida

		Punto p1 = new Punto(); // punto donde guardaremos el primer punto del LineaHorizonte s1
		Punto p2 = new Punto(); // punto donde guardaremos el primer punto del LineaHorizonte s2
		Punto paux = new Punto();
		imprimirLineas(s1,s2);

		// Mientras tengamos elementos en s1 y en s2
		while ((!s1.isEmpty()) && (!s2.isEmpty())) {
			paux = new Punto(); // Inicializamos la variable paux
			p1 = s1.getPunto(0); // guardamos el primer elemento de s1
			p2 = s2.getPunto(0); // guardamos el primer elemento de s2

			if (p1.getX() < p2.getX()) // si X del s1 es menor que la X del s2
			{
				paux.setX(p1.getX()); // guardamos en paux esa X
				paux.setY(Math.max(p1.getY(), s2y)); // y hacemos que el maximo entre la Y del s1 y la altura previa del
														// s2 sea la altura Y de paux

				if (paux.getY() != prev) // si este maximo no es igual al del segmento anterior
				{
					salida.addPunto(paux); // añadimos el punto al LineaHorizonte de salida
					prev = paux.getY(); // actualizamos prev
				}
				s1y = p1.getY(); // actualizamos la altura s1y
				s1.borrarPunto(0); // en cualquier caso eliminamos el punto de s1 (tanto si se añade como si no es
									// valido)
			} else if (p1.getX() > p2.getX()) // si X del s1 es mayor que la X del s2
			{
				paux.setX(p2.getX()); // guardamos en paux esa X
				paux.setY(Math.max(p2.getY(), s1y)); // y hacemos que el maximo entre la Y del s2 y la altura previa del
														// s1 sea la altura Y de paux

				if (paux.getY() != prev) // si este maximo no es igual al del segmento anterior
				{
					salida.addPunto(paux); // añadimos el punto al LineaHorizonte de salida
					prev = paux.getY(); // actualizamos prev
				}
				s2y = p2.getY(); // actualizamos la altura s2y
				s2.borrarPunto(0); // en cualquier caso eliminamos el punto de s2 (tanto si se añade como si no es
									// valido)
			} else // si la X del s1 es igual a la X del s2
			{
				if ((p1.getY() > p2.getY()) && (p1.getY() != prev)) // guardaremos aquel punto que tenga la altura mas
																	// alta
				{
					salida.addPunto(p1);
					prev = p1.getY();
				}
				if ((p1.getY() <= p2.getY()) && (p2.getY() != prev)) {
					salida.addPunto(p2);
					prev = p2.getY();
				}
				s1y = p1.getY(); // actualizamos la s1y e s2y
				s2y = p2.getY();
				s1.borrarPunto(0); // eliminamos el punto del s1 y del s2
				s2.borrarPunto(0);
			}
		}
		while ((!s1.isEmpty())) // si aun nos quedan elementos en el s1
		{
			paux = s1.getPunto(0); // guardamos en paux el primer punto

			if (paux.getY() != prev) // si paux no tiene la misma altura del segmento previo
			{
				salida.addPunto(paux); // lo añadimos al LineaHorizonte de salida
				prev = paux.getY(); // y actualizamos prev
			}
			s1.borrarPunto(0); // en cualquier caso eliminamos el punto de s1 (tanto si se añade como si no es
								// valido)
		}
		while ((!s2.isEmpty())) // si aun nos quedan elementos en el s2
		{
			paux = s2.getPunto(0); // guardamos en paux el primer punto

			if (paux.getY() != prev) // si paux no tiene la misma altura del segmento previo
			{
				salida.addPunto(paux); // lo añadimos al LineaHorizonte de salida
				prev = paux.getY(); // y actualizamos prev
			}
			s2.borrarPunto(0); // en cualquier caso eliminamos el punto de s2 (tanto si se añade como si no es
								// valido)
		}
		return salida;
	}
	
	
	
	private void imprimirLineas (LineaHorizonte s1, LineaHorizonte s2) {
		System.out.println("==== S1 ====");
		s1.imprimir();
		System.out.println("==== S2 ====");
		s2.imprimir();
		System.out.println("\n");
	}
	
	
	/*
	 * Método que carga los edificios que me pasan en el archivo cuyo nombre se
	 * encuentra en "fichero". El formato del fichero nos lo ha dado el profesor en
	 * la clase del 9/3/2020, pocos días antes del estado de alarma.
	 */

	public void cargarEdificios(String fichero) {
		try {
			int xi, y, xd;
			Scanner sr = new Scanner(new File(fichero));

			while (sr.hasNext()) {
				xi = sr.nextInt();
				xd = sr.nextInt();
				y = sr.nextInt();
				this.addEdificio(new Edificio(xi, y, xd));
			}
		} catch (Exception e) {
		}

	}

	public void metodoRandom(int n) {
		int i = 0;
		int xi, y, xd;
		for (i = 0; i < n; i++) {
			xi = (int) (Math.random() * 100);
			y = (int) (Math.random() * 100);
			xd = (int) (xi + (Math.random() * 100));
			this.addEdificio(new Edificio(xi, y, xd));
		}
	}
}
