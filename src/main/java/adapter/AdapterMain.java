package adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;

import businesslogic.BLFacade;
import domain.Driver;

public class AdapterMain {
	
	 public static void main(String[] args) {
		 //	the	BL	is	local
		 boolean isLocal =	true;
		 BLFacade blFacade =  BLFactory.getBLFacade(isLocal);
		 Driver	d= blFacade.getDriver("Urtzi");
		 DriverTable dt=new DriverTable(d);
		 dt.setVisible(true);
	 }
}
