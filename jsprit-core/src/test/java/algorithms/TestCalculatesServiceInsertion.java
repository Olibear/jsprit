/*******************************************************************************
 * Copyright (C) 2013  Stefan Schroeder
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 * Contributors:
 *     Stefan Schroeder - initial API and implementation
 ******************************************************************************/
package algorithms;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import basics.Job;
import basics.Service;
import basics.costs.VehicleRoutingTransportCosts;
import basics.route.DriverImpl;
import basics.route.TimeWindow;
import basics.route.TourActivities;
import basics.route.TourActivity;
import basics.route.Vehicle;
import basics.route.VehicleRoute;
import basics.route.DriverImpl.NoDriver;



public class TestCalculatesServiceInsertion {
	
	CalculatesServiceInsertion serviceInsertion;
	
	VehicleRoutingTransportCosts costs;
	
	Vehicle vehicle;
	
	Vehicle newVehicle;

	private Service first;

	private Service second;

	private Service third;

	private RouteStates states;

	private TourStateUpdater tourStateUpdater;

	private NoDriver driver;
	
	@Before
	public void setup(){
		Logger.getRootLogger().setLevel(Level.DEBUG);
		
		costs = mock(VehicleRoutingTransportCosts.class);
		vehicle = mock(Vehicle.class);
		when(vehicle.getCapacity()).thenReturn(1000);
		when(vehicle.getLocationId()).thenReturn("depot");
		when(vehicle.getEarliestDeparture()).thenReturn(0.0);
		when(vehicle.getLatestArrival()).thenReturn(100.0);
		
		newVehicle = mock(Vehicle.class);
		when(newVehicle.getCapacity()).thenReturn(1000);
		when(newVehicle.getLocationId()).thenReturn("depot");
		when(newVehicle.getEarliestDeparture()).thenReturn(0.0);
		when(newVehicle.getLatestArrival()).thenReturn(100.0);
		
		driver = DriverImpl.noDriver();
		
		when(costs.getTransportCost("depot", "1", 0.0, driver, vehicle)).thenReturn(10.0);
		when(costs.getTransportCost("depot", "2", 0.0, driver, vehicle)).thenReturn(20.0);
		when(costs.getTransportCost("depot", "3", 0.0, driver, vehicle)).thenReturn(10.0);
		when(costs.getTransportCost("1", "2", 0.0, driver, vehicle)).thenReturn(10.0);
		when(costs.getTransportCost("1", "3", 0.0, driver, vehicle)).thenReturn(20.0);
		when(costs.getTransportCost("2", "3", 0.0, driver, vehicle)).thenReturn(10.0);
		
		when(costs.getTransportCost("1", "depot", 0.0, driver, vehicle)).thenReturn(10.0);
		when(costs.getTransportCost("2", "depot", 0.0, driver, vehicle)).thenReturn(20.0);
		when(costs.getTransportCost("3", "depot", 0.0, driver, vehicle)).thenReturn(10.0);
		when(costs.getTransportCost("2", "1", 0.0, driver, vehicle)).thenReturn(10.0);
		when(costs.getTransportCost("3", "1", 0.0, driver, vehicle)).thenReturn(20.0);
		when(costs.getTransportCost("3", "2", 0.0, driver, vehicle)).thenReturn(10.0);
		
		when(costs.getTransportCost("depot", "1", 0.0, driver, newVehicle)).thenReturn(20.0);
		when(costs.getTransportCost("depot", "2", 0.0, driver, newVehicle)).thenReturn(40.0);
		when(costs.getTransportCost("depot", "3", 0.0, driver, newVehicle)).thenReturn(20.0);
		when(costs.getTransportCost("1", "2", 0.0, driver, newVehicle)).thenReturn(20.0);
		when(costs.getTransportCost("1", "3", 0.0, driver, newVehicle)).thenReturn(40.0);
		when(costs.getTransportCost("2", "3", 0.0, driver, newVehicle)).thenReturn(20.0);
		
		when(costs.getTransportCost("1", "depot", 0.0, driver, newVehicle)).thenReturn(20.0);
		when(costs.getTransportCost("2", "depot", 0.0, driver, newVehicle)).thenReturn(40.0);
		when(costs.getTransportCost("3", "depot", 0.0, driver, newVehicle)).thenReturn(20.0);
		when(costs.getTransportCost("2", "1", 0.0, driver, newVehicle)).thenReturn(20.0);
		when(costs.getTransportCost("3", "1", 0.0, driver, newVehicle)).thenReturn(40.0);
		when(costs.getTransportCost("3", "2", 0.0, driver, newVehicle)).thenReturn(20.0);
	
		when(costs.getTransportCost("depot", "1", 0.0, null, vehicle)).thenReturn(10.0);
		when(costs.getTransportCost("depot", "2", 0.0, null, vehicle)).thenReturn(20.0);
		when(costs.getTransportCost("depot", "3", 0.0, null, vehicle)).thenReturn(10.0);
		when(costs.getTransportCost("1", "2", 0.0, null, vehicle)).thenReturn(10.0);
		when(costs.getTransportCost("1", "3", 0.0, null, vehicle)).thenReturn(20.0);
		when(costs.getTransportCost("2", "3", 0.0, null, vehicle)).thenReturn(10.0);
		
		when(costs.getTransportCost("1", "depot", 0.0, null, vehicle)).thenReturn(10.0);
		when(costs.getTransportCost("2", "depot", 0.0, null, vehicle)).thenReturn(20.0);
		when(costs.getTransportCost("3", "depot", 0.0, null, vehicle)).thenReturn(10.0);
		when(costs.getTransportCost("2", "1", 0.0, null, vehicle)).thenReturn(10.0);
		when(costs.getTransportCost("3", "1", 0.0, null, vehicle)).thenReturn(20.0);
		when(costs.getTransportCost("3", "2", 0.0, null, vehicle)).thenReturn(10.0);
		
		when(costs.getTransportCost("depot", "1", 0.0, null, newVehicle)).thenReturn(20.0);
		when(costs.getTransportCost("depot", "2", 0.0, null, newVehicle)).thenReturn(40.0);
		when(costs.getTransportCost("depot", "3", 0.0, null, newVehicle)).thenReturn(20.0);
		when(costs.getTransportCost("1", "2", 0.0, null, newVehicle)).thenReturn(20.0);
		when(costs.getTransportCost("1", "3", 0.0, null, newVehicle)).thenReturn(40.0);
		when(costs.getTransportCost("2", "3", 0.0, null, newVehicle)).thenReturn(20.0);
		
		when(costs.getTransportCost("1", "depot", 0.0, null, newVehicle)).thenReturn(20.0);
		when(costs.getTransportCost("2", "depot", 0.0, null, newVehicle)).thenReturn(40.0);
		when(costs.getTransportCost("3", "depot", 0.0, null, newVehicle)).thenReturn(20.0);
		when(costs.getTransportCost("2", "1", 0.0, null, newVehicle)).thenReturn(20.0);
		when(costs.getTransportCost("3", "1", 0.0, null, newVehicle)).thenReturn(40.0);
		when(costs.getTransportCost("3", "2", 0.0, null, newVehicle)).thenReturn(20.0);
	
		
		first = Service.Builder.newInstance("1", 0).setLocationId("1").setTimeWindow(TimeWindow.newInstance(0.0, 100.0)).build();
		second = Service.Builder.newInstance("3", 0).setLocationId("3").setTimeWindow(TimeWindow.newInstance(0.0, 100.0)).build();
		third = Service.Builder.newInstance("2", 0).setLocationId("2").setTimeWindow(TimeWindow.newInstance(0.0, 100.0)).build();
		Collection<Job> jobs = new ArrayList<Job>();
		jobs.add(first);
		jobs.add(second);
		jobs.add(third);
		
		states = new RouteStates();
		states.initialiseStateOfJobs(jobs);
		
		ExampleActivityCostFunction activityCosts = new ExampleActivityCostFunction();

		serviceInsertion = new CalculatesServiceInsertion(costs, activityCosts);
		serviceInsertion.setActivityStates(states);
		
		tourStateUpdater = new TourStateUpdater(states, costs, activityCosts);
		
		
		
	}
	
	public TourActivity getActivityMock(String id, double earliestOperationStart, double currCost){
		TourActivity act = mock(TourActivity.class);
		when(act.getLocationId()).thenReturn(id);
		return act;
	}
	
	@Test
	public void whenInsertingTheFirstJobInAnEmptyTourWithVehicle_itCalculatesMarginalCostChanges(){
		TourActivities tour = new TourActivities();
		
		VehicleRoute route = VehicleRoute.newInstance(tour,driver,vehicle);
		tourStateUpdater.updateRoute(route);
		
		InsertionData iData = serviceInsertion.calculate(route, first, vehicle, vehicle.getEarliestDeparture(), null, Double.MAX_VALUE);
		assertEquals(20.0, iData.getInsertionCost(), 0.2);
		assertEquals(0, iData.getDeliveryInsertionIndex());
	}
	
	@Test
	public void whenInsertingTheSecondJobInAnNonEmptyTourWithVehicle_itCalculatesMarginalCostChanges(){
		TourActivities tour = new TourActivities();
		tour.addActivity(states.getActivity(first, true));
		
		VehicleRoute route = VehicleRoute.newInstance(tour,driver,vehicle);
		tourStateUpdater.updateRoute(route);
		
		InsertionData iData = serviceInsertion.calculate(route, second, vehicle, vehicle.getEarliestDeparture(), null, Double.MAX_VALUE);
		assertEquals(20.0, iData.getInsertionCost(), 0.2);
		assertEquals(0, iData.getDeliveryInsertionIndex());
	}
	
	@Test
	public void whenInsertingThirdJobWithVehicle_itCalculatesMarginalCostChanges(){
		TourActivities tour = new TourActivities();
		tour.addActivity(states.getActivity(first,true));
		tour.addActivity(states.getActivity(second,true));
		
		VehicleRoute route = VehicleRoute.newInstance(tour,driver,vehicle);
		
		tourStateUpdater.updateRoute(route);
		
		InsertionData iData = serviceInsertion.calculate(route, third, vehicle, vehicle.getEarliestDeparture(), null, Double.MAX_VALUE);
		assertEquals(0.0, iData.getInsertionCost(), 0.2);
		assertEquals(1, iData.getDeliveryInsertionIndex());
	}
	
	@Test
	public void whenInsertingThirdJobWithNewVehicle_itCalculatesMarginalCostChanges(){
		TourActivities tour = new TourActivities();
		tour.addActivity(states.getActivity(first,true));
		tour.addActivity(states.getActivity(second,true));
		
		VehicleRoute route = VehicleRoute.newInstance(tour,driver,vehicle);
		
		tourStateUpdater.updateRoute(route);
		
		InsertionData iData = serviceInsertion.calculate(route, third, newVehicle, newVehicle.getEarliestDeparture(), null, Double.MAX_VALUE);
		assertEquals(20.0, iData.getInsertionCost(), 0.2);
		assertEquals(1, iData.getDeliveryInsertionIndex());
	}
	
	@Test
	public void whenInsertingASecondJobWithAVehicle_itCalculatesLocalMarginalCostChanges(){
		TourActivities tour = new TourActivities();
		tour.addActivity(states.getActivity(first,true));
		tour.addActivity(states.getActivity(third,true));
		
		VehicleRoute route = VehicleRoute.newInstance(tour,driver,vehicle);
		tourStateUpdater.updateRoute(route);
		
		InsertionData iData = serviceInsertion.calculate(route, second, vehicle, vehicle.getEarliestDeparture(), null, Double.MAX_VALUE);
		assertEquals(0.0, iData.getInsertionCost(), 0.2);
		assertEquals(2, iData.getDeliveryInsertionIndex());
	}
	
	@Test
	public void whenInsertingASecondJobWithANewVehicle_itCalculatesLocalMarginalCostChanges(){
		TourActivities tour = new TourActivities();
		tour.addActivity(states.getActivity(first,true));
		tour.addActivity(states.getActivity(third,true));
		
		VehicleRoute route = VehicleRoute.newInstance(tour,driver,vehicle);
//		route.addActivity(states.getActivity(first,true));
//		route.addActivity(states.getActivity(third,true));
		tourStateUpdater.updateRoute(route);
		
		InsertionData iData = serviceInsertion.calculate(route, second, newVehicle, newVehicle.getEarliestDeparture(), null, Double.MAX_VALUE);
		assertEquals(20.0, iData.getInsertionCost(), 0.2);
		assertEquals(2, iData.getDeliveryInsertionIndex());
	}
	
	
	
}
