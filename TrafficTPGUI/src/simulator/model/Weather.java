package simulator.model;

public enum Weather {
	//guardamos los numeros necesarios para calcular valores y no tener muchos if que comprueben el weather
	SUNNY(2,2), CLOUDY(3,2), RAINY(10,2), WINDY(15,10), STORM(20,10);
	private int contInterCity, contCity;

	private Weather(int contInterCity, int contCity) {
		this.contInterCity = contInterCity;
		this.contCity = contCity;
	}

	public int getContInterCity() {
		return contInterCity;
	}

	public int getContCity() {
		return contCity;
	}
	public static Weather getWeatherByString(String name) {
		//se puede sustituir por valueOf, no caimos
		if(name.equals("SUNNY")) return Weather.SUNNY;
		if(name.equals("CLOUDY")) return Weather.CLOUDY;
		if(name.equals("RAINY")) return Weather.RAINY;
		if(name.equals("WINDY")) return Weather.WINDY;
		if(name.equals("STORM")) return Weather.STORM;
		return null;
	}
}
