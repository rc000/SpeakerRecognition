/*
Copyright (C) 2011  Kircher Engineering, LLC (http://www.kircherEngineering.com)

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package calculations;






/**
 * A class that performs the Fast Fourier Transform.
 * @author Kaleb Kircher
 *
 */
public class FFT
{
	double[] IMCSum;
	double[] RECSum;
	/**
	 * Overloaded method to generate signal values.
	 * 
	 * @return a double[] of signal values.
	 */
	public FFT( )
	{
	}
	public double[] generateSignal()
	{
		double[] signal =
		{ 2.28025, 1.32888, 0.39326, -0.49619, -1.31121, -2.02672, -2.62174,
				-3.08015, -3.39124, -3.55077, -3.55763, -3.42069, -3.15151,
				-2.76733, -2.28963, -1.74326, -1.15541, -0.55456, 0.03068,
				0.57271, 1.04606, 1.42835, 1.7122, 1.85105, 1.86948, 1.75376,
				1.50688, 1.13742, 0.65924, 0.09094, -0.54489, -1.22254,
				-1.91419, -2.59102, -3.22433, -3.78672, -4.25312, -4.60188,
				-4.81556, -4.8817, -4.79336, -4.54939, -4.15456, -3.61943,
				-2.95996, -2.19702, -1.35554, -0.46368, 0.44824, 1.34888,
				2.20699, 2.99264, 3.6783, 4.23987, 4.65761, 4.91685, 5.00855,
				4.92967, 4.68323, 4.27822, 3.72929, 3.05615, 2.28286, 1.43692,
				0.54824, -0.35197, -1.23239, -2.06272, -2.81485, -3.46385,
				-3.9889, -4.37404, -4.60875, -4.68828, -4.61378, -4.39223,
				-4.03611, -3.56286, -2.99417, -2.3551, -1.67308, -0.97682,
				-0.29515, 0.3441, 0.91522, 1.3956, 1.76664, 2.01448, 2.13052,
				2.1118, 1.96104, 1.68661, 1.30214, 0.826, 0.28055, -0.3087,
				-0.91416, -1.50721, -2.05935, -2.54337, -2.93442, -3.21099,
				-3.35583, -3.35666, -3.20667, -2.90487, -2.45619, -1.87128,
				-1.16626, -0.36208, 0.51622, 1.44039, 2.38004, 3.30375,
				4.18023, 4.97949, 5.67398, 6.23957, 6.65652, 6.91015, 6.99145,
				6.89738, 6.631, 6.20138, 5.62319, 4.91623, 4.10463, 3.216};
		return signal;
	}
	/**
	 * Overload method to generate a signal based on a function.
	 * @param Number of signal points to be generated.
	 * @return a double[] of signal values.
	 */
	public double[] generateSignal(int fs,int T)
	{
			double[] buffer=new double[1];
 
		return buffer;
	}

	/**
	 * Main method calculates the coefficients and phase. 
	 * @param args
	 */
	public double  [] calculateFramesFFT(double [] bufferFrames)
	{

		// instantiate classes to perform calculations
		CalcReal calcReal = new CalcReal();
		CalcImag calcImag = new CalcImag();
		SumCoefficiants sumC = new SumCoefficiants();
		
		// size of table
		int N = bufferFrames.length;
 		// samples per cycle
		int NN = bufferFrames.length;
 
		// harmonics
		//int K =  signal.length/40;
		int K=(bufferFrames.length-1)/2;
 
		// initialize arrays
		double[] amplitude = new double[K];
		double[] phase = new double[K];
		//double[] cosSignal = new double[N];
		double[] degreesSignal = new double[N];
		 IMCSum = new double[K];
		double[] radiansSignal = new double[N];
		RECSum = new double[K]; 
		
		double[][] IMC = new double[K][NN];
		double[][] REC = new double[K][NN];
 
		// generate the radians for the signal
		for (int i = 0; i < N; i++)
		{
			degreesSignal[i] = (360 / (double) N) * i;
			//System.out.println(degreesSignal[i]);
			radiansSignal[i] = degreesSignal[i] * (Math.PI / 180);
		}

		// get the real coefficients
		for (int i = 0; i < K; i++)
		{
			
			REC[i] = calcReal.calc(radiansSignal, bufferFrames, i + 1);
			RECSum[i] = (sumC.sum(REC[i], (double) NN));
			IMC[i] = calcImag.calc(radiansSignal, bufferFrames, i + 1);
			IMCSum[i] = (sumC.sum(IMC[i], (double) NN));
			amplitude[i] = 2 * (Math.sqrt(Math.pow(RECSum[i], 2)
					+ Math.pow(IMCSum[i], 2)));
		}

		

		// calculate phase
		// atan(abs(b/a))
		for (int i = 0; i < K; i++)
		{
			double temp = (Math.sqrt(Math.pow(IMCSum[i], 2)
					/ Math.pow(RECSum[i], 2)));
 			phase[i] = Math.atan(temp);
			// System.out.println(phase[i]);
		}

 
		return amplitude;
	}
	
	public double[] getPowerSpectrum()
	{
		// Only calculate the powers if they have not yet been calculated
		 
			int number_unfolded_bins = IMCSum.length / 2;
			double [] output_power = new double[number_unfolded_bins];
			for(int i = 0; i < output_power.length; i++)
				output_power[i] = (RECSum[i] * RECSum[i] + IMCSum[i] * IMCSum[i]) / RECSum.length;
		

		// Return the power
		return output_power;
	}
	public double[] getMagnitudeSpectrum()
	{
		// Only calculate the magnitudes if they have not yet been calculated
		 
		int number_unfolded_bins = IMCSum.length / 2;
			double [] output_magnitude = new double[number_unfolded_bins];
			for(int i = 0; i < output_magnitude.length; i++)
				output_magnitude[i] = ( Math.sqrt(RECSum[i] * RECSum[i] + IMCSum[i] * IMCSum[i]) ) / RECSum.length;
		

		// Return the magnitudes
		return output_magnitude;
	}

}
