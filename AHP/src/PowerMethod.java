class PowerMethod {
    public static double[] power(double[][] matrix){
	int n = matrix.length;
	double result[] = new double[n+1];

	// INITIAL VALUE
	double s;
	for(int i = 0; i < n ; i++){
	    s = 1;
	    for(int j = 0; j < n; j++){
		s = s * matrix[i][j];
	    }
	    result[i] = Math.pow(s,(double)1/n);
	}
	s = 0;
	for(int i = 0; i < n ; i++){
	    s = s + result[i];
	}
	for(int i = 0; i < n ; i++){
	    result[i] = result[i] / s;
	}

	// POWER METHOD
	int iter = 0;
	int flag = 0;
	double w;
	double rmax1=0;
	double rmax2=0;
	double ww[] = new double[n];
	while(flag == 0){
	    iter = iter + 1;
	    for(int i = 0 ; i < n ; i++){
		w = 0;
		for(int j = 0 ; j < n ; j++){
		    w = w + matrix[i][j]*result[j];
		}
		ww[i] = w;
	    }
	    s = 0;
	    for(int i = 0; i < n ; i++){
		s = s + ww[i];
	    }
	    rmax2 = s;
	    for(int i = 0; i < n ; i++){
		ww[i] = ww[i] / s;
	    }
	    flag = 1;
	    if (iter != 1){
		if(Math.abs(rmax2 - rmax1) > rmax1/1000) {
		    rmax1 = rmax2;
		    for(int k =0; k < n ; k++){
			result[k] = ww[k];
		    }
		    flag = 0;
		}
		for(int i = 0; i < n ; i++){
		    if(Math.abs(result[i]-ww[i])> result[i]/1000) {
			rmax1 = rmax2;
		    	for(int k =0; k < n ; k++){
			    result[k] = ww[k];
			}
			flag = 0;
		    }
		}
	    } else {
		rmax1 = rmax2;
		for(int k =0; k < n ; k++){
		   result[k] = ww[k];
		}
	    }
	}
	result[n] = rmax2;
	return result;
    }
}
