package test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import riso.builder.conceptNet5.URI.out.Estrutura;
import riso.builder.conceptNet5.URI.out.Nodo;
import riso.builder.conceptNet5.URI.out.Thesaurus;

@RunWith(JUnit4.class)
public class ThesauruTest {

	private Nodo jaguar;
	private Nodo animal;
	private Nodo mamifero;
	private Nodo organism;
	private Nodo liveThing;
	private Nodo life;
	private Nodo plant ;

	@Before
	public void setUp() {
		jaguar = new Nodo("/c/en/jaguar");
		animal = new Nodo("/c/en/animal");
		mamifero = new Nodo("/c/en/mammal");
		organism = new Nodo("/c/en/organism/n/a_living_thing_that_has_the_ability_to_act_or_function_independently");
		liveThing = new Nodo("/c/en/live_thing/n/a_living_entity");
		life = new Nodo("/c/en/life/n/living_things_collectively");
		plant = new Nodo("/c/en/plant");
	}


	@Test
	public final void constroiRelacoesTest() {

		//Caso 2 - OK - troca de posicao entre jaguar-animal para jaguar-mamifero
		Thesaurus t = new Thesaurus();

		Set<Nodo> nodosDiretos = new HashSet<Nodo>();
		Map<String, Set<Nodo>> relacoesDiretas = new HashMap<String, Set<Nodo>>();
		nodosDiretos.add(animal);
		relacoesDiretas.put("IsA", nodosDiretos);

		Estrutura estruDireta = new Estrutura();
		estruDireta.setRelacoes(relacoesDiretas);
		jaguar.setEstrutura(estruDireta);

		Set<Nodo> nodosInversos = new HashSet<Nodo>();
		Map<String, Set<Nodo>> relacoesInversas = new HashMap<String, Set<Nodo>>();
		nodosInversos.add(jaguar);
		relacoesInversas.put("IsA-1", nodosInversos);

		Estrutura estruInversa = new Estrutura();
		estruInversa.setRelacoes(relacoesInversas);
		animal.setEstrutura(estruInversa);

		String relacao = t.constroiRelacoes(jaguar, mamifero);

		Assert.assertEquals("IsA", relacao);
		Assert.assertEquals(t.constroiRelacoes(jaguar, mamifero), t.constroiRelacoes(mamifero, jaguar));
		Assert.assertEquals(t.constroiRelacoes(jaguar, plant),"");

	}
}
