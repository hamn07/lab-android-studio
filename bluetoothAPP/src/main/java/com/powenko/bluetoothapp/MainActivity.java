// 柯博文老師 www.powenko.com
package com.powenko.bluetoothapp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;  
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends Activity
{
    TextView myLabel;
    EditText myTextbox;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        myLabel = (TextView)findViewById(R.id.label);
        myTextbox = (EditText)findViewById(R.id.entry);
        
        myLabel.setMovementMethod(new ScrollingMovementMethod());
    }
    //找藍芽
    public void openBtn(View v)
    {
        try 
        {
            findBT();
            openBT();
        }
        catch (IOException ex) { }
    }
    //送資料
    public void sendBtn(View v)
    {
        try 
        {
            sendData();
        }
        catch (IOException ex) { }
        myTextbox.setText("");
    }
    //關閉藍芽
    public void closeBtn(View v)
    {
        try 
        {
            closeBT();
        }
        catch (IOException ex) { }
    }
    
    void findBT()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null)
        {
            myLabel.setText("No bluetooth adapter available");
        }
        
        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }
        
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {

                myLabel.setText(device.getName());
             //   if(device.getName().equals("powenko")) 你可以加上早指定的藍芽
             //   {
                    mmDevice = device;
                    break;
             //   }
            }
        }

    }
    
    void openBT() throws IOException
    {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
        if(mmDevice!=null){
	        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);        
	        mmSocket.connect();
	        mmOutputStream = mmSocket.getOutputStream();
	        mmInputStream = mmSocket.getInputStream();
	        
	        beginListenForData();
	        
	        myLabel.setText("Bluetooth Opened:"+mmDevice.getName()+"   "+mmDevice.getAddress());
        }
    }
    
    void beginListenForData()
    {
        final Handler handler = new Handler(); 
        final byte delimiter = 10; //This is the ASCII code for a newline character
        
        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {                
               while(!Thread.currentThread().isInterrupted() && !stopWorker)
               {
                    try 
                    {
                        int bytesAvailable = mmInputStream.available();                        
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;
                                    
                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {
                                            String t1=myLabel.getText().toString();
                                            t1=t1+"\n"+data;
                                            myLabel.setText(t1);
                                        }
                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                    
                                    
                                }
                            }
                        }
                    } 
                    catch (IOException ex) 
                    {
                        stopWorker = true;
                    }
               }
            }
        });

        workerThread.start();
    }
    
    void sendData() throws IOException
    {
    	if(mBluetoothAdapter == null)
    	{
    		findBT();
    		openBT();
    	}
    	String speed = myTextbox.getText().toString();
    	String msg = speed;
    	if(isNumeric(speed))
    	{
        	//msg = SpeedToArduino(speed);
    	}
        msg += "\n";
        mmOutputStream.write(msg.getBytes());
        String t1=myLabel.getText().toString();
        t1=t1+"\nData Sent";
        myLabel.setText(t1);
    }
    //check String is Numeric
    public static boolean isNumeric(String str)  
    {  
      try  
      {  
        double d = Double.parseDouble(str);  
      }  
      catch(NumberFormatException nfe)  
      {  
        return false;  
      }  
      return true;  
    }
    // change input speed to Arduino speed 
    private String SpeedToArduino(String speed)
    {
    	String str="";
    	
    	float fSpeed = Float.parseFloat(speed);
    	fSpeed = (float) (fSpeed*10*0.7);
    	int iSpeed = (int) fSpeed + 80;
    	if(iSpeed<=80){ iSpeed = 0;}
    	str = Integer.toString(iSpeed);
    	return str;
    }
    public void speedup(View v) throws IOException
    {
    	if(mBluetoothAdapter == null)
    	{
    		findBT();
    		openBT();
    	}
	        String msg = "q";
	        msg += "\n";
	        mmOutputStream.write(msg.getBytes());
	        String t1=myLabel.getText().toString();
	        t1=t1+"\nSpeed Up";
	        myLabel.setText(t1);
    }
    public void speeddown(View v) throws IOException
    {
    	if(mBluetoothAdapter == null)
    	{
    		findBT();
    		openBT();
    	}
        String msg = "w";
        msg += "\n";
        mmOutputStream.write(msg.getBytes());
        String t1=myLabel.getText().toString();
        t1=t1+"\nSpeed Down";
        myLabel.setText(t1);
    }
    public void cleanScreen(View v) throws IOException
    {
        myLabel.setText("");
        
        if(mBluetoothAdapter != null)
    	{
            String msg = "c";
            msg += "\n";
            mmOutputStream.write(msg.getBytes());
    	}
    }
    
    void closeBT() throws IOException
    {
    	//set speed to 0
        String msg = "e";
        msg += "\n";
        mmOutputStream.write(msg.getBytes());
    	//close
        stopWorker = true;
        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();
        String t1=myLabel.getText().toString();
        t1=t1+"\nBluetooth Closed";
        myLabel.setText(t1);
        mBluetoothAdapter = null;
    }
}