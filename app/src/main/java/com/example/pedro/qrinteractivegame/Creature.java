package com.example.pedro.qrinteractivegame;

import java.util.Arrays;

/**
 * Created by Pedro on 16/01/2016.
 */
public class Creature {
    private String specie_name;     //id 00
    private String name[];            //id 01 02
    private boolean genre;          //id 03

    private int force;              //id 04
    private int wisdom;             //id 05
    private int dextry;             //id 06
    private int health;             //id 07
    private int force_mod;          //id 08
    private int wisdom_mod;         //id 09
    private int dextry_mod;         //id 10
    private int health_mod;         //id 11

    private String element;         //id 12

    private String personality;     //id 13
    private String [] mutations;    //id 14 15 16 17
    private String [] skills;       //id 18 19 20 21 22 23

    private int level;              //id -01
    private int force_train;        //id -02
    private int wisdom_train;       //id -03
    private int dextry_train;       //id -04
    private int health_train;       //id -05
    private String item;            //id -08 07 06

    public Creature(String specie_name, String[] name, boolean genre, int force, int wisdom,
                    int dextry, int health, int force_mod, int wisdom_mod, int dextry_mod,
                    int health_mod, String element, String personality, String[] mutations,
                    String[] skills, int level, int force_train, int wisdom_train, int dextry_train,
                    int health_train, String item) {
        this.specie_name = specie_name;
        this.name = name;
        this.genre = genre;
        this.force = force;
        this.wisdom = wisdom;
        this.dextry = dextry;
        this.health = health;
        this.force_mod = force_mod;
        this.wisdom_mod = wisdom_mod;
        this.dextry_mod = dextry_mod;
        this.health_mod = health_mod;
        this.element = element;
        this.personality = personality;
        this.mutations = mutations;
        this.skills = skills;
        this.level = level;
        this.force_train = force_train;
        this.wisdom_train = wisdom_train;
        this.dextry_train = dextry_train;
        this.health_train = health_train;
        this.item = item;
    }

    public String getSpecie_name() {return specie_name;}

    public String [] getName() {return name;}

    public boolean isGenre() {return genre;}

    public int getForce() {return force;}

    public int getWisdom() {return wisdom;}

    public int getDextry() {return dextry;}

    public int getHealth() {return health;}

    public int getForce_mod() {
        return force_mod;
    }

    public int getWisdom_mod() {
        return wisdom_mod;
    }

    public int getDextry_mod() {
        return dextry_mod;
    }

    public int getHealth_mod() {
        return health_mod;
    }

    public String getElement() {
        return element;
    }

    public String getPersonality() {
        return personality;
    }

    public String[] getMutations() {
        return mutations;
    }

    public String[] getSkills() {
        return skills;
    }

    public int getLevel() {
        return level;
    }

    public int getForce_train() {
        return force_train;
    }

    public int getWisdom_train() {
        return wisdom_train;
    }

    public int getDextry_train() {
        return dextry_train;
    }

    public int getHealth_train() {
        return health_train;
    }

    public String getItem() {
        return item;
    }

    @Override
    public String toString() {
        return "Creature{" +
                "specie_name='" + specie_name + '\'' +
                ", name='" + name[0] + name[1]+'\'' +
                ", genre=" + genre +
                ", force=" + force +
                ", wisdom=" + wisdom +
                ", dextry=" + dextry +
                ", health=" + health +
                ", force_mod=" + force_mod +
                ", wisdom_mod=" + wisdom_mod +
                ", dextry_mod=" + dextry_mod +
                ", health_mod=" + health_mod +
                ", element='" + element + '\'' +
                ", personality='" + personality + '\'' +
                ", mutations=" + Arrays.toString(mutations) +
                ", skills=" + Arrays.toString(skills) +
                ", level=" + level +
                ", force_train=" + force_train +
                ", wisdom_train=" + wisdom_train +
                ", dextry_train=" + dextry_train +
                ", health_train=" + health_train +
                ", item='" + item + '\'' +
                '}';
    }
}
