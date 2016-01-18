package com.example.pedro.qrinteractivegame;

public class testing{
	public static void main(String [] arg){
		String code ="http://www.qrstuff.com";
		System.out.println(code);
		
		Creature monster=Creature_Factory.generate_creature(byteParser.qr_to_byte(code));
		System.out.println(monster.toString());
	}
}