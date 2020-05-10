/**
 *
 * @author Alex White
 */

package com.aow.flooringprogram.ui;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UserIOConsoleImpl implements UserIO{
    Scanner sc = new Scanner(System.in);
    
    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public double readDouble(String prompt) {
        print(prompt);
        return sc.nextDouble();
    }

    @Override
    public double readDouble(String prompt, double min, double max) {
        print(prompt);
        return sc.nextDouble();
    }

    @Override
    public float readFloat(String prompt) {
        print(prompt);
        return sc.nextFloat();
    }

    @Override
    public float readFloat(String prompt, float min, float max) {
        print(prompt);
        return sc.nextFloat();
    }

    @Override
    public int readInt(String prompt) {
        print(prompt);
        try{
            return sc.nextInt();
        } catch (InputMismatchException e){
            sc.nextLine();
            return 0;
        }
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        print(prompt);
        try{
            return sc.nextInt();
        } catch (InputMismatchException e){
            sc.nextLine();
            return 0;
        }
    }

    @Override
    public long readLong(String prompt) {
        print(prompt);
        return sc.nextLong();
    }

    @Override
    public long readLong(String prompt, long min, long max) {
        print(prompt);
        return sc.nextLong();
    }

    @Override
    public String readString(String prompt) {
        Scanner sc2 = new Scanner(System.in);
        print(prompt);
        return sc2.nextLine();
    }
}
    

