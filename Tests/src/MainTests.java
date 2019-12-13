import java.io.IOException;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;

public class MainTests {




    public static Color getColor(double intensity){
        double H = intensity * 0.4; // Hue (note 0.4 = Green, see huge chart below)
        double S = 1.0; // Saturation
        double B = 1.0; // Brightness

        return Color.getHSBColor((float)H, (float)S, (float)B);
        //return Color.getHSBColor(0, 0, (float) intensity);
    }


    public static void processGenreFolder(String genrePath, String genreFolder, String destination){
        try {
            //String genrePath = "music-genres\\genres\\";
            //String genreFolder = "rock";
            //String destination = "Spectrograms\\";

            File folder = new File(genrePath + genreFolder);
            File[] wavs = folder.listFiles();
            for (int fileCount = 0; fileCount < wavs.length; fileCount++){
                System.out.println(wavs[fileCount].getName());
                Spectrogram s = new Spectrogram(genrePath+genreFolder+"\\"+wavs[fileCount].getName(), true);

                generateSpectros(s, destination+genreFolder+"\\", wavs[fileCount].getName(), false);

            }



            //Spectrogram s = new Spectrogram("C:\\Users\\Jordan\\Desktop\\sinwavemono.wav", true);



            /*
            BufferedImage theImage = new BufferedImage(nX, nY, BufferedImage.TYPE_INT_RGB);
            double ratio;
            for(int x = 0; x<nX; x++){
                for(int y = 0; y<nY; y++){
                    ratio = plotData[x][y];
                    Color newColor = getColor(1.0-ratio);
                    theImage.setRGB(x, y, newColor.getRGB());
                }
            }
            File outputfile = new File("SpectrogramTest.png");
            ImageIO.write(theImage, "png", outputfile);
            */


        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public static void generateSpectros(Spectrogram s, String outputPath, String fileName, boolean addFull) throws IOException {
        double[] data = s.getByteArray();
        System.out.println(fileName + ": Finished Reading");
        int fullLength = data.length;
        int length = data.length;


        int cutOff = (int)s.getSR() * 15;
        if (cutOff < fullLength)
            length = cutOff;


        int WS = 2048;
        int OF = 8;
        int windowStep = WS/OF;

        double SR = s.getSR();
        double timeRes = WS/SR;
        double freqRes = SR/WS;
        double high = SR/2.0;
        double low = 5*SR/WS;

        int nX = (length-WS)/windowStep;
        int nY = WS/2+1;
        double[][] plotData = new double[nX][nY];

        double maxAmp = Double.MIN_VALUE;
        double minAmp = Double.MAX_VALUE;

        double ampSquare;
        double[] inputImag = new double[length];
        double threshold = 2.0;

        for (int i = 0; i < nX; i++){
            Arrays.fill(inputImag, 0.0);
            double[] WS_array = FFT.fft(Arrays.copyOfRange(data, i*windowStep, i*windowStep+WS), inputImag, true);
            for (int j = 0; j < nY; j++){
                ampSquare = (WS_array[2*j]*WS_array[2*j]) + (WS_array[2*j+1]*WS_array[2*j+1]);

                plotData[i][j] = 10 * Math.log10(Math.max(ampSquare, threshold));

                if (plotData[i][j] > maxAmp)
                    maxAmp = plotData[i][j];
                else if (plotData[i][j] < minAmp)
                    minAmp = plotData[i][j];
            }
        }

        double diff = maxAmp - minAmp;
        for (int x = 0; x < nX; x++){
            for (int y = 0; y < nY; y++){
                plotData[x][y] = (plotData[x][y]-minAmp)/diff;
            }
        }

        System.out.println(fileName + ": Finished Transform");

        for (int a = 0; a < 5; a++){
            BufferedImage image = new BufferedImage(nX/5, nY, BufferedImage.TYPE_INT_RGB);
            double ratio;
            for (int x = 0; x < (nX/5); x++){
                for (int y = 0; y < nY; y++){
                    int xLoc = x+(nX/5)*a;
                    ratio = plotData[xLoc][y];
                    Color newColor = getColor(1.0-ratio);
                    image.setRGB(x, y, newColor.getRGB());
                }
            }

            File outputfile = new File(outputPath + fileName+"-"+a+".png");
            ImageIO.write(image, "png", outputfile);
        }

        if (addFull){
            BufferedImage image = new BufferedImage(nX, nY, BufferedImage.TYPE_INT_RGB);
            double ratio;
            for (int x = 0; x < nX; x++){
                for (int y = 0; y < nY; y++){
                    ratio = plotData[x][y];
                    Color newColor = getColor(1.0-ratio);
                    image.setRGB(x, y, newColor.getRGB());
                }
            }

            File outputfile = new File(outputPath + "Full-" + fileName + ".png");
            ImageIO.write(image, "png", outputfile);
        }
        System.out.println(fileName + ": Finished Images");
    }
    public static void main(String[] args){

    }

    public static void getSpectroFromFile(String filePath, String fileName){
        try{
            Spectrogram s = new Spectrogram(filePath, true);
            generateSpectros(s, "spectrograms\\", fileName, true);
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
