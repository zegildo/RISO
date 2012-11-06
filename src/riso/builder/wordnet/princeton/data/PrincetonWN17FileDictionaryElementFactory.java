/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package riso.builder.wordnet.princeton.data;

import riso.builder.wordnet.JWNLException;

import java.util.Map;

/** <code>FileDictionaryElementFactory</code> that produces elements for the Princeton release of WordNet v 1.7 */
public class PrincetonWN17FileDictionaryElementFactory extends AbstractPrincetonFileDictionaryElementFactory {
	public PrincetonWN17FileDictionaryElementFactory() {
	}

	public Object create(Map params) throws JWNLException {
		return new PrincetonWN17FileDictionaryElementFactory();
	}
}
