package com.example.admin.calculator;

import android.graphics.Point;
import android.icu.text.DecimalFormat;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.*;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainDebug";
    TextView Screenn;
    Button btn;

    int Begining;
    int End;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Screenn = (TextView) findViewById(R.id.Screen);
        Button BTN_Del = (Button) findViewById(R.id.Btn_Del);
        String BackSpace = "&#8656;";
        BTN_Del.setText(Html.fromHtml(BackSpace));
    }

    public void updateTextView(View view) {
        String existData = Screenn.getText().toString();
        int BTNid = view.getId();
        btn = (Button) findViewById(BTNid);
        if (BTNid == R.id.BTN_Multi || BTNid == R.id.BTN_Add || BTNid == R.id.BTN_Div || BTNid == R.id.BTN_Subt) {
            char lastchar = existData.charAt(existData.length() - 1);
            if (lastchar != '+' && lastchar != '-' && lastchar != '/' && lastchar != 'x' && lastchar != '.') {
                Screenn.setText(existData + btn.getText().toString());
            }
        } else {

            if (BTNid == R.id.BTN_0) {

                if (!existData.equals("0")) {
                    Screenn.setText(existData + btn.getText().toString());
                }
            } else if (BTNid == R.id.BTN_Dot) {

                if (!existData.equals("0")) {
                    String Screen = Screenn.getText().toString();
                    int Pointer = Screen.length()-1;
                    while (Screen.charAt(Pointer) != 'x' && Screen.charAt(Pointer) != '+' && Screen.charAt(Pointer) != '-' && Screen.charAt(Pointer) != '/' && Pointer!=0)
                    {
                        --Pointer;
                    }
                    if(!Screen.substring(Pointer,Screen.length()).contains("."))
                    {
                        Screenn.setText(existData + btn.getText().toString());
                    }
                }else
                {
                    Screenn.setText(existData + btn.getText().toString());
                }
            } else {
                double Zero;

                if (existData.equals("0")) {
                    Screenn.setText(btn.getText().toString());
                } else {
                    Screenn.setText(existData + btn.getText().toString());
                }
            }
        }

    }

    public void BackspaceBTN(View view) {
        String TextView = Screenn.getText().toString();

        if (TextView != null && TextView.length() > 0) {
            TextView = TextView.substring(0, TextView.length() - 1);
            if (TextView.equals(""))
                TextView = "0";
            Screenn.setText(TextView);
        }

    }

    public void ClearBTN(View view) {
        Screenn.setText("0");
        Begining = 0;
        End = 0;
    }

    public void chngSignBTN(View view)
    {
        String Screen = Screenn.getText().toString();
        if(Screen.lastIndexOf('-')<=0 && !Screen.contains("x") && !Screen.contains("+") && !Screen.contains("/"))
        {
            if(Screen.charAt(0)== '-')
            {
                Screen = Screen.substring(1,Screen.length());
            }
            else
            {
                Screen ='-' + Screen;
            }
            Screenn.setText(Screen);
        }
    }

    public double getSecond(int signPointer, String Screen) {

        int Pointer = signPointer + 1;
        char nextChar = Screen.charAt(Pointer);
        while (nextChar != '+' && nextChar != '-' && nextChar != '/' && nextChar != 'x' && Pointer < Screen.length()) {
            Pointer++;
            if (Pointer == Screen.length())
                break;
            nextChar = Screen.charAt(Pointer);

        }
        String secondDouble = Screen.substring(signPointer + 1, Pointer);
        End = Pointer - 1;
        Double Result = 0.0;
        Result = Double.parseDouble(secondDouble);
        return Result;

    }

    public double getFirst(int signPointer, String Screen) {

        int Pointer = signPointer - 1;
        char PerviousChar = Screen.charAt(Pointer);
        while (PerviousChar != '+' && PerviousChar != '-' && PerviousChar != '/' && PerviousChar != 'x' && Pointer != 0) {
            --Pointer;
            PerviousChar = Screen.charAt(Pointer);

        }
        if (Pointer != 0) {
            Pointer++;
        }
        String firstDouble = Screen.substring(Pointer, signPointer);
        Begining = Pointer;
        Double Result = 0.0;
        Result = Double.parseDouble(firstDouble);
        return Result;

    }

    public Double Calculate(String Screen, double First, double Second, int Sign) {
        Double Result = 0.0;
        switch (Screen.charAt(Sign)) {
            case 'x':
                Result = First * Second;
                break;
            case '/':
                Result = First / Second;
                break;
            case '+':
                Result = First + Second;
                break;
            case '-':
                Result = First - Second;
                break;
        }
        return Result;
    }

    public String Replace(String Screen, Double Result) {
        String NewEquation;
        DecimalFormat decimalFormat = new DecimalFormat("######.####");
        String Res = decimalFormat.format(Result);
        int screenLength = Screen.length();
        if (Begining <= 0 && End >= screenLength) {
            NewEquation = Res;
        } else if (Begining <= 0) {
            NewEquation = Res + Screen.substring(End + 1, screenLength);
        } else if (End >= screenLength) {
            NewEquation = Screen.substring(0, Begining) + Res;
        } else {
            NewEquation = Screen.substring(0, Begining) + Res + Screen.substring(End + 1, screenLength);
        }

        return NewEquation;
    }

    public String FindPSign(String Screen) {
        String newScreen = Screen;
        int SignPointer = 0;
        int M = Screen.indexOf('x');
        int D = Screen.indexOf('/');
        if ((M < D && M != -1) || D < 0) {
            SignPointer = M;
        } else if ((D < M && D != -1) || M < 0) {
            SignPointer = D;
        }


        if (SignPointer > 0) {
            double firstDouble = getFirst(SignPointer, Screen);
            double secondDouble = getSecond(SignPointer, Screen);
            double result = Calculate(Screen, firstDouble, secondDouble, SignPointer);
            newScreen = Replace(Screen, result);
        }
        return newScreen;
    }

    public String FindSign(String Screen) {
        String newScreen = Screen;
        int SignPointer = -2;
        int A = Screen.indexOf('+');
        int S;
        if (Screen.lastIndexOf('-') == 0) {
            if (A > 0) {
                SignPointer = A;
            }
        } else {
            if (Screen.charAt(0) == '-') {
                S = Screen.replaceFirst("-", "m").indexOf("-");
            } else
                S = Screen.indexOf("-");


            if ((A < S && A > 0) || S <= 0) {
                SignPointer = A;
            } else if ((S < A && S > 0) || A < 0) {
                SignPointer = S;
            }


        }


        if (SignPointer >= 1) {
            double firstDouble = getFirst(SignPointer, Screen);
            double secondDouble = getSecond(SignPointer, Screen);
            double result = Calculate(Screen, firstDouble, secondDouble, SignPointer);
            newScreen = Replace(Screen, result);
        }
        return newScreen;
    }

    public void equalBTN(View view) {

        String Screen = Screenn.getText().toString();
        while(Screen.charAt(Screen.length()-1)=='x' || Screen.charAt(Screen.length()-1)=='+' || Screen.charAt(Screen.length()-1)=='-' ||Screen.charAt(Screen.length()-1)=='/'||Screen.charAt(Screen.length()-1)=='.')
        {
            Screen = Screen.substring(0,Screen.length()-1);
        }
        while (Screen.contains("x") || Screen.contains("/")) {
            Screen = FindPSign(Screen);
        }
        while (Screen.contains("+") || (Screen.contains("-") && Screen.lastIndexOf("-") != 0)) {
            Screen = FindSign(Screen);
        }
        Screenn.setText(Screen);
    }
}
