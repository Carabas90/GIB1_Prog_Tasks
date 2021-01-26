/*
	Marius Albeser, 301749
	
	Lernfragen:
	
	1. Das Schl�sselwort this stellt einen Bezug zum Objekt selbst her.
	Ruft sich quasi selber auf... 
	In der Simulations Klasse wird beim Aufruf des Constructors der Actor Klasse
	bzw. den Klassen Doc, Patient und Germ ein Bezug zum jeweiligen Objekt per this hergestellt.
	this bezieht sich in dem Fall auf die Variable context der PApplet Klasse und bezieht sich somit
	auf die individuelle Instanz des Actor Objekts, welches momentan im ProcessingApplet dargestellt wird.
	This bezieht sich also immer auf das momentan initialisierte/ benutzte
	Objekt!
	
	Das Schl�sselwort super stellt immer einen Bezug zur direkten Mutterklasse
	der ausgew�hlten Klasse her. So wird zum Beispiel in der Doc Klasse in beiden
	verf�gbaren Konstruktoren (Zeile 11 & 16) per Schl�sselwort super ein Bezug
	zu den Konstrukoren der Actor (Mutterklasse) Klasse hergestellt.
	In dem Fall um Bezug zu den Konstruktoren der Zeile 25 & 30 der Actor Klasse herzustellen und
	diese wiederum aufzurufen.
	
	2. Da ich f�r mein Programm, die Cruiser Klasse nicht f�r passend empfunden habe,
	habe ich die Cruiser Klasse nicht benutzt. Stattdessen habe ich die von Actor erbenden
	Klassen Patient, Doc und Germ erstellt. Welche dank der Vererbung alle Attribute, Methoden und
	Konstrukotren ihrer Mutterklasse geerbt haben. 
	So zum Beispiel die Attribute xPosition und yPosition, welche per Konstruktor als
	random Wert initial an die Unterklassen weitergegeben werden. Oder auch die Methoden
	act und display der Actor Klasse werden quasi von der Mutterklasse als default mitgegeben.
	Die Unterklassen m�ssen diese Attribute bzw. Methoden nun nicht nochmal extra deklarieren um sie 
	benutzen zu k�nnen.
	--> F�rdert Wiederverwendbarkeit/ Lesbarkeit.
	Die Methoden act und display m�ssen nicht gezwungenerma�en in den Unterklassen verwendet werden.
	Wenn sie aber implementiert werden, �berschreiben sie die Methoden der Actor Klasse
	Und werden von Java zur Laufzeit per Methodensuche "zuerst" angew�hlt
	---> Polymorphie!
	
	3. Wie oben schon kurz beschrieben wird Polymorphie insbesondere mit Vererbung zusammen benutzt.
	Im Fall dieses Programmes, werden zwischen Actorklasse und den erbenden Unterklassen
	Methoden �berschrieben (Dynamische Polymorphie). So nutzen also die Unterklassen von Actor
	die Methoden der Mutterklasse und �berschreiben diese! Schnittstellensignatur bleibt gleich!
	Implementierung �ndert sich! 
	So ist es m�glich, auf einem Objekt Patient und einem Objekt Doc
	in der Main method die selbe Methode aufzurufen, aber eine andere Ausf�hrung im Programm zu erhalten.
	---> VIELGESTALTIGKEIT


 */


/*Infektions-Spiel
 * Virenst�mme infizieren Patienten und Patienten k�nnen andere Patienten anstecken.
 * �rzte sind immun gegen eine Ansteckung und k�nnen bei Kontakt Patienten heilen, 
 * bzw. Virenst�mme beseitigen.
 * Default setting - Patient nicht immun nach Heilung durch Arzt.
 * Kann in den Zeilen 146 & 147 ge�ndert werden.
 */


package prog1.vererbung;

import processing.core.PApplet;

public class Simulation extends PApplet {
	public static final int MAX_ACTORS = 100;
	private Actor[] actors;
	
	private int docs = 3;
	private int patients = 40;
	private int germs = 7;
	
	
	
	
	public void setup() {
		size(800, 800);
		actors = new Actor[MAX_ACTORS];

		//actors[0] = new Actor(this, "doc.png");// nicht m�glich, da Actor abstract
		

		for (int i = 0; i < docs; i++) {
			actors[i] = new Doc(this, "doc.png", 50);// turn regelt die Drehfreudigkeit des Docs
			actors[i].isDoctor = true;
		}
		
		for(int i = docs; i < docs + patients; i++) {
			actors[i] = new Patient(this, "patient.png");
			actors[i].isPatient = true;
		}

		for(int i = docs + patients; i < docs + patients + germs; i++) {
			actors[i] = new Germ(this, "germ.png");
			actors[i].isGerm = true;
		}
	}

	public void draw() {
		background(200);
		

		
		// First act ...
		for (int i = 0; i < actors.length; i++) {
			if (actors[i] != null) {
				actors[i].act();
				actors[i].changeDirection();
			}
		}

		// ... then visualize the current state of each actor
		for (int i = 0; i < actors.length && actors[i] != null; i++) {
			if (actors[i] != null) {
				actors[i].display();
				checkCollision();
			}
		}
		
	}
	//sehr umst�ndlicher Weg f�r die Collision detection. 
	//Leider war es mir nicht m�glich, den actors Array (mit den Objekten) fehlerfrei an die Actor Klasse/ bzw. dessen Unterklassen 
	//weiterzugeben.
	public void checkCollision() {
		
		for (int i = 0; i < actors.length; i++) {
			for (int j =0; j < i; j++) {
				
					if(actors[i] != null && actors[j] != null) {
					double distance = Math.pow(actors[i].positionX - actors[j].positionX, 2) + Math.pow(actors[i].positionY - actors[j].positionY, 2);
					double minDistance = Math.pow((actors[i].radius/2) + (actors[j].radius/2), 2);

					if (distance <= minDistance) {
						
						if(actors[i].isGerm==true && actors[j].isPatient == true) {
						actors[j].isHealthy = false;
						}
						
						if(actors[i].isPatient == true && actors[i].isHealthy == true && actors [j].isPatient && actors[j].isHealthy != true
								|| actors[j].isPatient == true && actors[j].isHealthy == true && actors [i].isPatient && actors[i].isHealthy != true) {
							actors[i].isHealthy = false;
							actors[j].isHealthy = false;
							
							
						}
						
						if(actors[i].isDoctor == true && actors[j].isPatient == true && actors[j].isHealthy == false
								|| actors[j].isDoctor == true && actors[i].isPatient == true && actors[i].isHealthy == false) {
							actors[j].isHealthy = true;
							actors[i].isHealthy = true;
							actors[j].isPatient = true;//auf false �ndern um Immunit�t der Patienten nach Heilung zu aktivieren
							actors[i].isPatient	= true;//auf false �ndern um Immunit�t der Patienten nach Heilung zu aktivieren
							
							}
						
						if(actors[i].isDoctor == true && actors[j].isGerm == true || actors[i].isGerm == true && actors[j].isDoctor == true) {
							actors[j].germDesinfected = true;
							actors[i].germDesinfected = true;
							actors[j].isGerm = false;
							actors[i].isGerm = false;
							System.out.println("desinfect");
						}
				}
			}
		}
	}
}
	

	static public void main(String[] passedArgs) {
		System.out.println(Simulation.class.getName());
		PApplet.main(new String[] { Simulation.class.getName() });
	}

}
