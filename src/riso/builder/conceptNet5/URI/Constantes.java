package riso.builder.conceptNet5.URI;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Constantes {

	public static final String BARRA = "/";
	public static final String RAIZ = "http://conceptnet5.media.mit.edu/data/5.1";
	public static final String AFIRMACOES = "a";
	public static final String CONCEITOS = "c";
	public static final String CONTEXTOS = "ctx";
	public static final String CONTEXTOS_TOTAL = "ctx/all";
	public static final String CONJUNTO_DADOS = "d";
	public static final String ID= "e";
	public static final String RELACOES = "r";
	public static final String TIPO_CONTRIBUICAO = "s";
	public static final String SEARCH = "search";
	public static final String ASSOC = "assoc";
	public static final String LIMIT = "limit=";
	public static final String OFFSET = "&offset=";


	
	public static final String IDIOMA_INGLES = "en";
	public static final String MORFOLOGIA_VERBO = "v";
	
	public static final String RELACAO_IS_A = "IsA";
	public static final String RELACAO_INSTANCE_OF = "InstanceOf";
	public static final String RELACAO_PART_OF = "PartOf";
	public static final String RELACAO_DIRECT_OBJETCT_OF = "DirectObjectOf";
	public static final String RELACAO_SUBJECT_OF = "SubjectOf";
	public static final String RELACAO_HAS_PROPERTY = "HasProperty";
	public static final String RELACAO_HAS_PREQUISITE = "HasPrerequisite";
	public static final String RELACAO_TRANSLATION_OF = "TranslationOf";
	public static final String RELACAO_AT_LOCATION = "AtLocation";
	public static final String RELACAO_DERIVEDED_FROM = "DerivedFrom";
	public static final String RELACAO_MEMBER_OF = "MemberOf";
	public static final String RELACAO_CREATED_BY = "CreatedBy";
	public static final String RELACAO_CAPABLE_OF = "CapableOf";
	public static final String RELACAO_USED_FOR = "UsedFor";
	public static final String RELACAO_HAS_A = "HasA";
	public static final String RELACAO_DEFINED_AS="DefinedAs";
	public static final String RELACAO_SILIMAR_SIZE= "SimilarSize";
	public static final String RELACAO_NOT_IS_A = "NotIsA";
	public static final String RELACAO_NOT_DESIRES= "NotDesires";
	public static final String RELACAO_RELATED_TO = "RelatedTo";
	public static final String RELACAO_RECEIVES_ACTION = "ReceivesAction";
	public static final String RELACAO_MOTIVATED_BY_GOAL = "MotivatedByGoal";
	public static final String RELACAO_LOCATED_NEAR = "LocatedNear";
	public static final String RELACAO_CONCEPTUALLY_RELATED_TO = "ConceptuallyRelatedTo";
	public static final String RELACAO_SYNONYM = "Synonym";
	//Novas relacoes - relacoes inversas criadas para compor a tabela de relacoes sem‰ntias
	public static final String RELACAO_COMPOUND_OF = "CompoundOf";
	public static final String RELACAO_HAS_MEMBERS = "HasMembers";
	public static final String RELACAO_CAN_BE = "CanBe";
	public static final String RELACAO_IS_A_PROPERTY_OF = "IsAProperty";
	public static final String RELACAO_BELONGS_TO = "BelogsTo";
	public static final String RELACAO_CLASS_OF = "ClassOf";
	public static final String RELACAO_SOURCE_OF = "SourceOf";

	
	public static final String[] RELACOES_SEMANTICAS_FORTES = {RELACAO_IS_A,RELACAO_MEMBER_OF,RELACAO_PART_OF,RELACAO_INSTANCE_OF,RELACAO_DERIVEDED_FROM};
	
	public static final Map<String, String> RELACOES_SEMANTICAS_DIRETAS;
    static {
        Map<String, String> relacoesSemanticas = new HashMap<String, String>();
        relacoesSemanticas.put(RELACAO_HAS_PROPERTY, RELACAO_IS_A_PROPERTY_OF);
        relacoesSemanticas.put(RELACAO_INSTANCE_OF,RELACAO_CLASS_OF);
        relacoesSemanticas.put(RELACAO_DERIVEDED_FROM, RELACAO_SOURCE_OF);
        relacoesSemanticas.put(RELACAO_SYNONYM, RELACAO_SYNONYM);
        relacoesSemanticas.put(RELACAO_RELATED_TO, RELACAO_RELATED_TO);
        relacoesSemanticas.put(RELACAO_HAS_A, RELACAO_BELONGS_TO);
        relacoesSemanticas.put(RELACAO_PART_OF, RELACAO_COMPOUND_OF);
        relacoesSemanticas.put(RELACAO_MEMBER_OF, RELACAO_HAS_MEMBERS);
        relacoesSemanticas.put(RELACAO_IS_A, RELACAO_CAN_BE);
        RELACOES_SEMANTICAS_DIRETAS = Collections.unmodifiableMap(relacoesSemanticas);
    }
    
    public static final Map<String, String> RELACOES_SEMANTICAS_INVERSA;
    static {
        Map<String, String> relacoesSemanticas = new HashMap<String, String>();
        relacoesSemanticas.put(RELACAO_IS_A_PROPERTY_OF,RELACAO_HAS_PROPERTY);
        relacoesSemanticas.put(RELACAO_CLASS_OF,RELACAO_INSTANCE_OF);
        relacoesSemanticas.put(RELACAO_SOURCE_OF,RELACAO_DERIVEDED_FROM);
        relacoesSemanticas.put(RELACAO_SYNONYM,RELACAO_SYNONYM);
        relacoesSemanticas.put(RELACAO_RELATED_TO,RELACAO_RELATED_TO);
        relacoesSemanticas.put(RELACAO_BELONGS_TO,RELACAO_HAS_A);
        relacoesSemanticas.put(RELACAO_COMPOUND_OF,RELACAO_PART_OF);
        relacoesSemanticas.put(RELACAO_HAS_MEMBERS,RELACAO_MEMBER_OF);
        relacoesSemanticas.put(RELACAO_CAN_BE,RELACAO_IS_A);
        RELACOES_SEMANTICAS_INVERSA = Collections.unmodifiableMap(relacoesSemanticas);
    }
		
	public static final String DATA_WORDNET = "wordnet";
	public static final String DATA_DBPEDIA = "dbpedia";
	public static final String DATA_WIKTIONARY = "wiktionary";
	public static final String DATA_REVERB = "reverb";
	public static final String DATA_CONCEPTNET = "concepnet";
	
	public static final String SOURCE_CONSTRIBUICAO_HUMANA = "contributor";
	public static final String SOURCE_WEB_SITES = "web";
	public static final String SOURCE_REGRA = "rule";
	public static final String SOURCE_WORDNET = "wordnet";
	public static final String SOURCE_DBPEDIA = "wordnet";
	
	
    public static final String SEARCH_IDf="id";
    public static final String SEARCH_URI = "uri";
    public static final String SEARCH_REL = "rel";
    public static final String SEARCH_START = "start";
    public static final String SEARCH_END = "end";
    public static final String SEARCH_CONTEXT = "context";
    public static final String SEARCH_DATASET = "dataset";
    public static final String SEARCH_LICENSE = "license";
    public static final String SEARCH_NODES = "nodes";
    public static final String SEARCH_START_LEMMAS = "startLemmas"; 
    public static final String SEARCH_END_LEMAS ="endLemmas"; 
    public static final String SEARCH_REL_LEMMAS ="relLemmas";
    /**
     * Verifica o texto passado como representante de alguma aresta
     */
    public static final String SEARCH_TEXT = "text";
    /**
     * Verifica a utlizacao do texto passado com uma frase de utilizacao
     */
    public static final String SEARCH_SURFACE_TEXT = "surfaceText";
    public static final String SEARCH_MIN_WHEIGHT = "minWeight";
    public static final String SEARCH_LIMIT = "limit";
    public static final String SEARCH_OFFSET = "offset";
    public static final String SEARCH_FEATURES = "features";
    public static final String SEARCH_FILTER = "filter";
    
    /*
     * Associacao
     */
    public static final String ASSOC_LIST = "list";
    public static final String ASSOC_LIMIT = "limit";
    public static final String ASSOC_FILTER = "filter";

    
    public static final String ARROBA = "@";

    public static final int ZERO = 0;
    
    

}
