package util;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;



public class BenchmarkResult {
	private double[] results;
	private double[] vehicles;
	private double[] times;
	
	private DescriptiveStatistics statsResults;
	private DescriptiveStatistics statsVehicles;
	private DescriptiveStatistics statsTimes;
	
	public final BenchmarkInstance instance;
	
	public final int runs;
	
	public BenchmarkResult(BenchmarkInstance instance, int runs, double[] results, double[] compTimes, double[] vehicles) {
		super();
		this.results = results;
		this.runs = runs;
		this.times = compTimes;
		this.instance = instance;
		this.vehicles = vehicles;
		this.statsResults = new DescriptiveStatistics(results);
		this.statsTimes = new DescriptiveStatistics(times);
		this.statsVehicles = new DescriptiveStatistics(vehicles);
	}
	
	public double[] getResults(){
		return results;
	}
	
	public double[] getVehicles(){
		return vehicles;
	}
	
	public double[] getCompTimes(){
		return times;
	}
	
	public DescriptiveStatistics getResultStats(){
		return statsResults;
	}
	
	public DescriptiveStatistics getVehicleStats(){
		return statsVehicles;
	}
	
	public DescriptiveStatistics getTimesStats(){
		return statsTimes;
	}
	
}