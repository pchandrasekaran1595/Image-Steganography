// PROGRAM USED IN THE IMAGE FILE SUBDIVISION

import java.io.*;
import java.io.IOException;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JPanel;

import java.lang.*;
import java.util.ArrayList;
import java.lang.StringBuilder;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

class ProjectExtension
{
	public static void main(String[] args)throws Exception
	{
		String origImagePathSetup = "C:\\Users\\Ourself\\Desktop\\IIT - Second\\DSP\\Project\\";
		String imgToHideSetup = "C:\\Users\\Ourself\\Desktop\\IIT - Second\\DSP\\Project\\";
		String RecovrImg = "C:\\Users\\Ourself\\Desktop\\IIT - Second\\DSP\\Project\\Recovered Image.jpg";
		String origImagePathCopy = "";
		
		Scanner input = new Scanner(System.in);
		
		BufferedImage orImage = null, hidImage = null, rxImage = null;
		File oImage = null;
		File hImage = null;
		File rImage = null;
		int MAX_VAL = 255;
		int widthFactor = 1;
		int heightFactor = 1;
		int count = 0;
		int runTimeExecBuff = 25;
		int runTimeExecBuffExit = 5000;
		int factorR  = 15;
		int factorG  = 15;
		int factorB  = 25;
		int Alpha    =  255;
			
		ArrayList<Integer> hidR = new ArrayList<Integer>();
		ArrayList<Integer> hidG = new ArrayList<Integer>();
		ArrayList<Integer> hidB = new ArrayList<Integer>();

		ArrayList<Integer> hidR_Rx = new ArrayList<Integer>();
		ArrayList<Integer> hidG_Rx = new ArrayList<Integer>();
		ArrayList<Integer> hidB_Rx = new ArrayList<Integer>();
		
		System.out.print("\n\nEnter the name of the Image to be used  : ");
		java.io.BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String imgBig = in.readLine();
		StringBuilder sb = new StringBuilder(origImagePathSetup);
		sb.append(imgBig);
		String origImagePath = sb.toString();

		System.out.print("\n\nEnter the name of the Image to be hidden : ");
		String imgSmall = in.readLine();
		StringBuilder sbsm = new StringBuilder(imgToHideSetup);
		sbsm.append(imgSmall);
		String imgToHide = sbsm.toString();
		try
		{
			oImage     = new File(origImagePath);
			orImage    = ImageIO.read(oImage);
			hImage     = new File(imgToHide);
			hidImage   = ImageIO.read(hImage);
		}
		catch(IOException e)
		{
			System.out.println("----  ERROR!!!!! ----\n");
			System.out.println("No Image Found\n");
			e.printStackTrace();
			System.out.println("\n\n-----  RESTART -----");
			System.exit(0);
		}
		
		System.out.print("\n\nModify widthFactor and/or heightFactor? (Y , N) : ");
		isTrying: while(true)
		{
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
		
		int orHeight = orImage.getHeight();
		int orWidth  = orImage.getWidth();
		int hiHeight = hidImage.getHeight();
		int hiWidth  = hidImage.getWidth();
		int totalUsablePixels = orHeight * orWidth / (heightFactor * widthFactor);
		int no_Of_HidPix = hiHeight * hiWidth;
		if(totalUsablePixels < no_Of_HidPix)
		{
			System.out.println("\n\nDATA ERROR!!!! \n\nPossible Causes: \n1. Image to be hidden is too large \n2. Unoptimized widthFactor and/or heightFactor");
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

		JFrame    orImFr = new JFrame("Original Image");
		ImageIcon orImIc = new ImageIcon(imgBig);
		JLabel    orImLa = new JLabel(orImIc);
		orImFr.add(orImLa);
		orImFr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		orImFr.pack();
		orImFr.setVisible(true);
		
		JFrame    hiImFr = new JFrame("Image to be Hidden");
		ImageIcon hiImIc = new ImageIcon(imgSmall);
		JLabel    hiImLa = new JLabel(hiImIc);
		hiImFr.add(hiImLa);
		hiImFr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		hiImFr.pack();
		hiImFr.setVisible(true);

		for(int y=0 ; y<hiHeight ; y++)
		{
			for(int x=0 ; x<hiWidth ; x++)
			{
				Color c = new Color(hidImage.getRGB(x,y), true);
				int Red = (int)(Math.floor(c.getRed()));
				int Green = (int)(Math.floor(c.getGreen()));
				int Blue = (int)(Math.floor(c.getBlue()));
				hidR.add(Red);
				hidG.add(Green);
				hidB.add(Blue);
			}
		}

		Integer[] RData = new Integer[hidR.size()];
		RData = hidR.toArray(RData);
		
		Integer[] GData = new Integer[hidG.size()];
		GData = hidG.toArray(GData);
			
		Integer[] BData = new Integer[hidG.size()];
		BData = hidB.toArray(BData);

		int[] Qr = new int[hidR.size()];
		int[] Qg = new int[hidG.size()];
		int[] Qb = new int[hidB.size()];
			
		Iter: for(int y=0 ; y<orHeight ; y+=heightFactor)
		{
			for(int x=0 ; x<orWidth ; x+=widthFactor)
			{
				if(count == no_Of_HidPix || x >= orWidth)
					break;
				else
				{
					Color c = new Color(orImage.getRGB(x,y), true);
					int Red = (int)(Math.floor(c.getRed()));
					int Green = (int)(Math.floor(c.getGreen()));
					int Blue = (int)(Math.floor(c.getBlue()));
					
					Qr[count] = (Red + (int)RData[count] + factorR) / MAX_VAL;
					Qg[count] = (Green + (int)GData[count] + factorG) / MAX_VAL;
					Qb[count] = (Blue + (int)BData[count] + factorB) / MAX_VAL;
					
					int newRed = (Red + (int)RData[count] + factorR) % MAX_VAL;
					int newGreen = (Green + (int)GData[count] + factorG) % MAX_VAL;
					int newBlue = (Blue + (int)BData[count] + factorB) % MAX_VAL;
				
					Color newC = new Color(newRed, newGreen, newBlue, Alpha);
					orImage.setRGB(x,y,newC.getRGB());
					count++;
				}
			}
			if(count < no_Of_HidPix)
				continue Iter;
			else
				break;
		}
		
		int index = 0;
		char[] rxImFileSetup = new char[imgBig.length()-4];
		while(imgBig.charAt(index) != '.')
		{	
			rxImFileSetup[index] = imgBig.charAt(index);
			index++;
		}
		String rxImFile = new String(rxImFileSetup); 
		StringBuilder sb_1 = new StringBuilder(rxImFile);
		sb_1.append(" - With Information.jpg");
		String rxIm = sb_1.toString();
		try
		{
			File outputfile = new File(rxIm);
			ImageIO.write(orImage, "jpg", outputfile);
			JFrame modfr = new JFrame(rxIm);
			ImageIcon modicon = new ImageIcon(rxIm);
			JLabel modlabel = new JLabel(modicon);
			modfr.add(modlabel);
			modfr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			modfr.pack();
			modfr.setVisible(true);
			rImage = new File(origImagePath);
			rxImage = ImageIO.read(rImage);
		}
		catch(IOException e)
		{
			System.out.println("----  ERROR!!!!! ----");
			e.printStackTrace();
		}
	
		count = 0;
		Iter1: for(int y=0 ; y<orHeight ; y+=heightFactor)
		{
			for(int x=0 ; x<orWidth ; x+=widthFactor)
			{
				if(count == no_Of_HidPix || x >= orWidth)
					break;
				else
				{
					Color modic = new Color(orImage.getRGB(x,y), true);
					int Red_1 = (int)(Math.floor(modic.getRed()));
					int Green_1 = (int)(Math.floor(modic.getGreen()));
					int Blue_1 = (int)(Math.floor(modic.getBlue()));
				
					Color origc = new Color(rxImage.getRGB(x,y), true);
					int Red_2 = (int)(Math.floor(origc.getRed()));
					int Green_2 = (int)(Math.floor(origc.getGreen()));
					int Blue_2 = (int)(Math.floor(origc.getBlue()));
				
					int RxDSetup_R = (MAX_VAL * Qr[count] + Red_1);
					int RxD_R = RxDSetup_R - Red_2 - factorR;
					hidR_Rx.add(RxD_R);

					int RxDSetup_G = (MAX_VAL * Qg[count] + Green_1);
					int RxD_G = RxDSetup_G - Green_2 - factorG;
					hidG_Rx.add(RxD_G);
			
					int RxDSetup_B = (MAX_VAL * Qb[count] + Blue_1);
					int RxD_B = RxDSetup_B - Blue_2 - factorB;
					hidB_Rx.add(RxD_B);
					
					count++;
				}
			}
			if(count < no_Of_HidPix)
				continue Iter1;
			else
				break;
		}	

		Integer[] R_RxData = new Integer[hidR_Rx.size()];
		R_RxData = hidR_Rx.toArray(R_RxData);
		Integer[] G_RxData = new Integer[hidG_Rx.size()];
		G_RxData = hidG_Rx.toArray(G_RxData);
		Integer[] B_RxData = new Integer[hidB_Rx.size()];
		B_RxData = hidB_Rx.toArray(B_RxData);
		
		count = 0;
		
		BufferedImage ReceivedImage = new BufferedImage(hiWidth, hiHeight, BufferedImage.TYPE_INT_RGB);
		for(int y=0 ; y<ReceivedImage.getHeight() ; y++)
		{
			for(int x=0 ; x<ReceivedImage.getWidth() ; x++)
			{
				int Red = (int)R_RxData[count];
				int Green = (int)G_RxData[count];
				int Blue = (int)B_RxData[count];
			
				Color newC = new Color(Red, Green, Blue);
				ReceivedImage.setRGB(x,y,newC.getRGB());
				count++;
			}
		}
		try
		{
			File opfile = new File(RecovrImg);
			ImageIO.write(ReceivedImage, "jpg", opfile);
			JFrame rxfr = new JFrame("Recovered Image");
			ImageIcon rxicon = new ImageIcon("Recovered Image.jpg");
			JLabel rxlabel = new JLabel(rxicon);
			rxfr.add(rxlabel);
			rxfr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			rxfr.pack();
			rxfr.setVisible(true);
		}
		catch(IOException e)
		{
			System.out.println("----  ERROR!!!!! ----");
			e.printStackTrace();
		}
		
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
		