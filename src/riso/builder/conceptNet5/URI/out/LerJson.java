package riso.builder.conceptNet5.URI.out;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


import com.google.gson.Gson;

public class LerJson {

	public static void main(String[] args) {

		Gson gson = new Gson();

		try {

			//URL url = new URL("http://conceptnet5.media.mit.edu/data/5.1/c/en/toast");
			URL url = new URL("http://conceptnet5.media.mit.edu/data/5.1/assoc/list/en/boy,girl@-1?limit=5&rel=/r/PartOf/&offset=3&end=/c/en/car/");
			BufferedReader br = new BufferedReader(
					new InputStreamReader(url.openStream()));

			//convert the json string back to object
			//Aresta obj = gson.fromJson(br, Aresta.class);
			Associacao ass = gson.fromJson(br, Associacao.class);

			System.out.println(ass);
			System.out.println(LerJson.class);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
