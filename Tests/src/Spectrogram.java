import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Spectrogram {

    private byte[] fileData;


    public Spectrogram(String filepath, boolean printInfo)throws IOException{
        Path path = Paths.get(filepath);
        fileData = Files.readAllBytes(path);
        if (printInfo){
            System.out.println("Channels: " + (int)fileData[22] + " " + (int)fileData[23]);
            System.out.println("SR: "+ getSR());
            System.out.println("BPS: " + (int)fileData[34]);
        }

    }

    public double getSR(){
        ByteBuffer wrap = ByteBuffer.wrap(Arrays.copyOfRange(fileData, 24, 28));
        return wrap.order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();
    }

    public double[] getByteArray(){
        byte[] raw = Arrays.copyOfRange(fileData, 44, fileData.length);
        int totalLength = raw.length;
        int newLength = 0;
        boolean stereo = false;


        int channels = (int)fileData[22];
        if (channels == 1)
            stereo = false;
        else if (channels == 2)
            stereo = true;

        if (stereo){
            newLength = totalLength/4;
        }
        else{
            newLength = totalLength/2;
        }

        double[] data_mono = new double[newLength];
        double left, right;

        if (stereo){
            System.out.println("Stereo");
            for (int i = 0; 4*i+3 < totalLength; i++){
                left = (short)((raw[4*i+1] & 0xff) << 8) | (raw[4*i] & 0xff);
                right = (short)((raw[4*i+3] & 0xff) << 8) | (raw[4*i+2] & 0xff);

                data_mono[i] = (left+right)/2.0;
            }
        }
        else{
            for (int i = 0; 2*i+1 < totalLength; i++){
                left = (short)((raw[2*i+1] & 0xff) << 8) | (raw[2*i] & 0xff);

                data_mono[i] = left;
            }
        }

        return data_mono;
    }


}
