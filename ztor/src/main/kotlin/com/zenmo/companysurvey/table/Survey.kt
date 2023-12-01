package com.zenmo.companysurvey.table

import org.jetbrains.exposed.sql.*

/**
 * [com.zenmo.companysurvey.dto.Survey]
 */
object CompanySurveyTable: Table("company_survey") {
    val id = uuid("id").autoGenerate()
    // Can be fetched at https://energiekeregio.nl/api/v1/zenmo?details=15989
    val energiekeRegioId = uinteger("energieke_regio_id").nullable()
    // ZEnMo project name
    val project = varchar("project", 50)

    val companyName = varchar("company_name", 50)
    val personName = varchar("person_name", 50)
    val email = varchar("email", 50)

    /**
     * [com.zenmo.companysurvey.dto.Transport]
     */
    val hasVehicles = bool("has_vehicles").nullable()
    val numDailyCarCommuters = uinteger("num_daily_car_commuters").nullable()

    /**
     * [com.zenmo.companysurvey.dto.Trucks]
     */
    val numTrucks = uinteger("num_trucks").nullable()
    val numElectricTrucks = uinteger("num_electric_trucks").nullable()
    val numTruckChargePoints = uinteger("num_truck_charge_points").nullable()
    val powerPerTruckChargePointKw = float("power_per_truck_charge_point_kw").nullable()
    val annualTravelDistancePerTruckKm = uinteger("annual_travel_distance_per_truck_km").nullable()
    val numPlannedElectricTrucks = uinteger("num_planned_electric_trucks").nullable()

    /**
     * [com.zenmo.companysurvey.dto.Vans]
     */
    val numVans = uinteger("num_vans").nullable()
    val numElectricVans = uinteger("num_electric_vans").nullable()
    val numVanChargePoints = uinteger("num_electric_van_charge_points").nullable()
    val powerPerVanChargePointKw = float("power_per_van_charge_point_kw").nullable()
    val annualTravelDistancePerVanKm = uinteger("annual_travel_distance_per_van_km").nullable()
    val numPlannedElectricVans = uinteger("num_planned_electric_vans").nullable()

    /**
     * [com.zenmo.companysurvey.dto.Cars]
     */
    val numCars = uinteger("num_cars").nullable()
    val numElectricCars = uinteger("num_electric_cars").nullable()
    val numCarChargePoints = uinteger("num_car_charge_points").nullable()
    val powerPerCarChargePointKw = float("power_per_car_charge_point_kw").nullable()
    val annualTravelDistancePerCarKm = uinteger("annual_travel_distance_per_car_km").nullable()
    val numPlannedElectricCars = uinteger("num_planned_electric_cars").nullable()

    override val primaryKey = PrimaryKey(id)
}

//object QuarterHourlyValuesFileTable: Table("quarter_hourly_values_file") {
//    val id = uuid("id").autoGenerate()
//
//    val name = varchar("name", 100)
//    val url = varchar("url", 100)
//}
