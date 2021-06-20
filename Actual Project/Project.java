// PROGRAM USED IN THE USER INPUT AND TEXT FILE SUBDIVISIONS

import java.io.*;
import java.io.IOException;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import java.lang.*;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Project
{
	public static void main(String args[])throws IOException
	{
		String origImagePathSetup = "C:\\Users\\Ourself\\Desktop\\IIT - Second\\DSP\\Project\\";
		String pyDataPath = "C:\\Users\\Ourself\\Desktop\\IIT - Second\\DSP\\Project\\2 - PyTxData.txt";
		String outDataPath = "C:\\Users\\Ourself\\Desktop\\IIT - Second\\DSP\\Project\\3 - JaRxData.exe";
		String fileType = "UTF-8";

		Scanner input = new Scanner(System.in);
		System.out.print("\n\nEnter the name of the Image to be used : ");
		String img = input.nextLine();
		StringBuilder sb = new StringBuilder(origImagePathSetup);
		sb.append(img);
		String origImagePath = sb.toString();
		
		ArrayList<Integer> rawDataSetup = new ArrayList<Integer>();
		ArrayList<Integer> recDataSetup = new ArrayList<Integer>();
		
		BufferedImage image = null;
		BufferedImage orig = null;
		File f = null;
		File rec = null;
		int MAX_VAL = 255;
		int widthFactor = 1;
		int heightFactor = 1;
		int Alpha = 0;
		int factor = -15;
		int count = 0;
		int runTimeExecBuff = 25;
		int runTimeExecBuffExit = 5000;

		try
		{
			f = new File(origImagePath);
			image = ImageIO.read(f);
			InputStream fin = new FileInputStream(pyDataPath);
			InputStreamReader reader = new InputStreamReader(fin, fileType);
			int data;
			while((data = reader.read()) != -1)
			{
				rawDataSetup.add(data); 
			}
			reader.close();
		}
		catch(IOException e)
		{
			System.out.println("----  ERROR!!!!! ----\n");
			System.out.println("No File Found\n");
			e.printStackTrace();
			System.out.println("\n\n-----  RESTART -----");
			System.exit(0);
		}	
	
		Integer[] rawData = new Integer[rawDataSetup.size()];
		rawData = rawDataSetup.toArray(rawData);
		int[] Q = new int[rawDataSetup.size()];

		
		isTrying: while(true)
		{
			System.out.print("\n\nModify widthFactor and/or heightFactor? (Y , N) : ");
			char choice = input.next().charAt(0);
			if(choice == 'Y' || choice == 'y')
			{ 
				System.out.print("\n\nEnter the widthFactor   : ");
				widthFactor = input.nextInt();
				System.out.print("\n\nEnter the heightFactor  : ");
				heightFactor = input.nextInt();
				break;
			}
			else if(choice == 'N' || choice == 'n')
				break;
			else
			{
				System.out.print("\n----- Invalid Choice -----");
				continue isTrying;
			}
		}
		
		int height = image.getHeight();
		int width = image.getWidth();
		int totalUsablePixels = height * width / (heightFactor * widthFactor);
		if(totalUsablePixels < rawDataSetup.size())
		{
			System.out.println("\n\nDATA ERROR!!!! \n\nPossible Causes: \n1. Too much Data \n2. Unoptimized widthFactor and/or heightFactor");
			System.out.println("\n\n-----  RESTART -----");
			try
			{
				Thread.sleep(runTimeExecBuffExit);
				System.exit(0);
			}
			catch(InterruptedException e)
			{
				System.out.println("----  ERROR!!!!! ----");
				e.printStackTrace();
			}
			
		}
		
		JFrame frame = new JFrame("Original Image");
		ImageIcon icon = new ImageIcon(img);
		JLabel label = new JLabel(icon);
		frame.add(label);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		
		Iter: for(int y=0 ; y<height ; y+=heightFactor)
		{
			for(int x=0 ; x<width ; x+=widthFactor)
			{
				if(count == rawDataSetup.size() || x >= width)
					break;
				else
				{
					Color c = new Color(image.getRGB(x,y), true);
					int Red = (int)(Math.floor(c.getRed()));
					int Green = (int)(Math.floor(c.getGreen()));
					int Blue = (int)(Math.floor(c.getBlue()));
					
					Q[count] = (Red + rawData[count] + factor) / MAX_VAL;
					//Q[count] = (Green + rawData[count] + factor) / MAX_VAL;
					//Q[count] = (Blue + rawData[count] + factor) / MAX_VAL;
					
					int newRed = (Red + rawData[count] + factor) % MAX_VAL;
					//int newGreen = (Green + rawData[count] + factor) % MAX_VAL;
					//int newBlue = (Blue + rawData[count] + factor) % MAX_VAL;
				
					Color newC = new Color(newRed, Green, Blue, Alpha);
					//Color newC = new Color(newRed, newGreen, newBlue);
					image.setRGB(x,y,newC.getRGB());
					count++;
				}
					
			}
			if(count < rawDataSetup.size())
				continue Iter;
			else
				break;
		}

		int index = 0;
		char[] rxImFileSetup = new char[img.length()-4];
		while(img.charAt(index) != '.')
		{	
			rxImFileSetup[index] = img.charAt(index);
			index++;
		}
		String rxImFile = new String(rxImFileSetup); 
		StringBuilder sb_1 = new StringBuilder(rxImFile);
		sb_1.append(" - With Information.jpg");
		String rxIm = sb_1.toString();
		
		try
		{
			File outputfile = new File(rxIm);
			ImageIO.write(image, "jpg", outputfile);
			JFrame modfr = new JFrame("Modified Image with Information");
			ImageIcon modicon = new ImageIcon(rxIm);
			JLabel modlabel = new JLabel(modicon);
			modfr.add(modlabel);
			modfr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			modfr.pack();
			modfr.setVisible(true);
			rec = new File(origImagePath);
			orig = ImageIO.read(rec);
		}
		catch(IOException e)
		{
			System.out.println("----  ERROR!!!!! ----");
			e.printStackTrace();
		}
		
		count = 0;
		Iter1: for(int y=0 ; y<height ; y+=heightFactor)
		{
			for(int x=0 ; x<width ; x+=widthFactor)
			{
				if(count == rawDataSetup.size() || x >= width)
					break;
				else
				{
					Color modic = new Color(image.getRGB(x,y));
					int Red_1 = (int)(Math.floor(modic.getRed()));
					//int Green_1 = (int)(Math.floor(modic.getGreen()));
					//int Blue_1 = (int)(Math.floor(modic.getBlue()));
				
					Color origc = new Color(orig.getRGB(x,y));
					int Red_2 = (int)(Math.floor(origc.getRed()));
					//int Green_2 = (int)(Math.floor(origc.getGreen()));
					//int Blue_2 = (int)(Math.floor(origc.getBlue()));
				
					int RxDSetup = (MAX_VAL * Q[count] + Red_1);
					int RxD = RxDSetup - Red_2 - factor;
					recDataSetup.add(RxD);
					count++;
				}
			}
			if(count < rawDataSetup.size())
				continue Iter1;
			else
				break;
		}

		
		
		Integer[] recData = new Integer[recDataSetup.size()];
		recData = recDataSetup.toArray(recData);
		
		OutputStream opstream = new FileOutputStream(outDataPath);
		OutputStreamWriter writer = new OutputStreamWriter(opstream, fileType);
		for (Integer s : recData)
		{
			int rx = (int)s;
			char[] ch = Character.toChars(rx);
			for(int i=0 ; i<ch.length ; i++)
				writer.write(ch[i]);
		}
		writer.close();
		
		System.out.print("\n\n---------------- Image Processing Successful ----------------");
		
		isTryingToEnd: while(true)
		{
			System.out.print("\n\n\nPress Any Char to Terminate Execution : ");
			if(Character.isLetterOrDigit(input.next().charAt(0)))
			{
				try
				{
					Thread.sleep(runTimeExecBuff);
					System.exit(0);
				}
				catch(InterruptedException e)
				{
					System.out.println("----  ERROR!!!!! ----");
					e.printStackTrace();
				}
			}
			else
				continue isTryingToEnd;
		}
	}
}	