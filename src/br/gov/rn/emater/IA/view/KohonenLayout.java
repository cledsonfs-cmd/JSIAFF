package br.gov.rn.emater.IA.view;


public class KohonenLayout extends NetworkLayout {

	public int getLayerLayout(int idx) {
		if (idx == 0)
			return ROW_LAYOUT;
		return SQUARE_LAYOUT;
	}

}