package com.example.pedro.qrinteractivegame;

import android.app.DialogFragment;
import android.app.Fragment;
import android.util.Log;
import android.util.Range;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Pedro on 16/01/2016.
 */
public class Creature_Factory {
    private static String[][] species_table={
            {"cat",    "10",    "10",   "10",   "5"},
            {"Troll",    "1",     "2",    "1",    "50"},
            {"Goblin", "15", "12", "33", "8"},
            {"Slime", "0", "1", "2", "3"},
            {"Dragon", "100", "100", "100", "100"},
            {"Squeleton", "1", "10", "20", "3"},
            {"Ghost", "0", "99", "0","0"},
            {"Ork", "70", "3", "20", "10"},
            {"Zombie", "10", "30","4","99"},
            {"Spider", "8", "8", "8", "88"},
            {"Wolf", "10","20","50","20"}
    };

    private static String[] personality_table={
            "Violent",
            "Shy",
            "Hardy",
            "Lonely",
            "Brave",
            "Adamant",
            "Naughty",
            "Bold",
            "Docile",
            "Relaxed",
            "Impish"
    };

    private static String[] element_table={
            "Fire",
            "Water",
            "Ice",
            "Wind",
            "Thunder",
            "Ligth",
            "Darkness",
            "Soul",
            "Void",
            "Ground"
    };

    private static  String[] mutation_table={
            "Two heads",
            "Prensile Claws",
            "Wings",
            "One eye",
            "Horns",
            "Iron Skin",
            "Two rows of Tooth",
            "Fire hair",
            "Mouth in the Stomach"
    };

    private  static String[] skill_table={
            "Slash",
            "Magic blast",
            "Bite",
            "Punch",
            "Spell",
            "Attack",
            "Kick",
            "Smash",
            "Course",
            "Apocalypse"
    };

    public static  String [] skill_mod_table={
            "fire",
            "toxic",
            "letal",
            "Force",
            "killer",
            "air",
            "dangerous",
            "laser",
            "metalic",
            "secret"
    };

    public static String [] name_table={
            "saur",
            "ril",
            "bob",
            "lil",
            "chu",
            "tir",
            "lol",
            "cat",
            "mon",
            "dile"
    };

    public static String [] item_table ={
            "nothing",
            "potion",
            "bomb",
            "gun",
            "crystal",
            "letter",
            "food",
            "armor",
            "axe",
            "coin",
            "book",
            "scroll",
            "sword"
    };
    public static Creature generate_creature(byte [] code){
        Creature monster;
        if (code.length<31){
            System.out.print(code.length);
            monster=null;
        }else{
            ArrayList<Integer> id_list=new ArrayList<Integer>();
            for (byte b:code
                 ) {
                id_list.add( b & 0xFF);
            }
            int l=id_list.size();
            String [] specie_data=species_table[id_list.get(00)%species_table.length];

            String [] name=new String[2];
            name[0] =name_table[id_list.get(1)%name_table.length];
            name[1]=name_table[id_list.get(2)%name_table.length];
            boolean genre = id_list.get(3)%2==0 ? false : true;

            int force=Integer.parseInt(specie_data[1]);
            int wisdom=Integer.parseInt(specie_data[2]);
            int dextry=Integer.parseInt(specie_data[3]);
            int health=Integer.parseInt(specie_data[4]);

            int force_mod=id_list.get(8);
            int wisdom_mod=id_list.get(9);
            int dextry_mod=id_list.get(10);
            int health_mod=id_list.get(11);

            String element=element_table[id_list.get(12)%element_table.length];
            String personality=personality_table[id_list.get(13)%personality_table.length];

            String [] mutations = new  String[4];
            for (int i=0;i<mutations.length;i++){
                mutations[i]=mutation_table[id_list.get(i+14)%mutation_table.length];
            }

            String [] skills = new  String[3];
            for (int i=0;i<skills.length;i++){
                String main=skill_table[id_list.get(18+i*2)%skill_table.length];
                skills[i]=skill_mod_table[id_list.get(19+i*2)%skill_mod_table.length]+" "+main;
            }
            int level=id_list.get(l-1)%100;
            int force_train=id_list.get(l-2);
            int wisdom_train=id_list.get(l-3);
            int dextry_train=id_list.get(l-4);
            int health_train=id_list.get(l-5);

            String item=item_table[id_list.get(l-8)%item_table.length];
            monster = new Creature(specie_data[0],name,genre,force,wisdom,dextry,health,force_mod,
                    wisdom_mod,dextry_mod,health_mod,element,personality,mutations,skills,level,
                    force_train,wisdom_train,dextry_train,health_train,item);
        }
        return monster;
    }

    public static byte [] creature_to_byte(Creature monster){
        byte [] code=new byte [31];

        for (int i=0;i<species_table.length;i++){
            if (species_table[i][0].equals(monster.getSpecie_name())){
                code[0]=(byte) i;
                break;
            }
        }

        for (int i=0; i<name_table.length;i++) {
            for (int j = 0; j < monster.getName().length; j++) {
                if (monster.getName()[j] == name_table[i]) {
                    code[j + 1] = (byte) i;
                }
            }
        }

        code[3]=monster.isGenre() ? (byte) 1 : (byte) 0;

        code[4]= 0x00;
        code[5]= 0x00;
        code[6]= 0x00;
        code[7]= 0x00;

        code[8]=(byte) monster.getForce_mod();
        code[9]=(byte) monster.getWisdom_mod();
        code[10]=(byte) monster.getDextry_mod();
        code[11]=(byte) monster.getHealth_mod();

        for (int i=0;i<element_table.length;i++){
            if (element_table[i].equals(monster.getElement())){
                code[12]=(byte) i;
                break;
            }
        }

        for (int i=0;i<personality_table.length;i++){
            if (personality_table[i].equals(monster.getPersonality())){
                code[13]=(byte) i;
                break;
            }
        }

        for (int i=0;i<monster.getMutations().length;i++){
            for (int j=0;j<mutation_table.length;j++){
                if (mutation_table[j].equals(monster.getMutations()[i])){
                    code[14+i]=(byte) j;
                    break;
                }
            }
        }

        for (int i=0;i<monster.getSkills().length;i++){
            for (int j=0;j<skill_table.length;j++){
                if (monster.getSkills()[i].contains(skill_table[j])){
                    code[18+i*2]=(byte) j;
                    break;
                }
            }
			for (int j=0;j<skill_mod_table.length;j++){
				if (monster.getSkills()[i].contains(skill_mod_table[j])){
					code[19+i*2]=(byte) j;
                    break;
				}
            }
        }
		code[30]=(byte) monster.getLevel();
		code[29]=(byte) monster.getForce_train();
		code[28]=(byte) monster.getWisdom_train();
		code[27]=(byte) monster.getDextry_train();
        for (int i=0;i<item_table.length;i++){
            if (monster.getItem().contains(item_table[i])){
                code[24]=(byte) i;
                break;
            }
        }
        return  code;
    }
}
