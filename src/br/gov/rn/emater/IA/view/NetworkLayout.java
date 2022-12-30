package br.gov.rn.emater.IA.view;

abstract public class NetworkLayout {

	final static public int ROW_LAYOUT = 1;
	final static public int SQUARE_LAYOUT = 2;


	abstract public int getLayerLayout(int idx);

}