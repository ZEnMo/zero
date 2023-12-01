package com.zenmo.companysurvey

import com.zenmo.companysurvey.dto.*
import org.jetbrains.exposed.sql.Database
import org.junit.BeforeClass
import kotlin.test.Test

fun connectToDb(): Database {
    val url = System.getenv("POSTGRES_URL")
    val user = System.getenv("POSTGRES_USER")
    val password = System.getenv("POSTGRES_PASSWORD")

    return Database.connect(url, driver = "org.postgresql.Driver", user, password)
}

class RepositoryTest {
    companion object {
        @JvmStatic
        @BeforeClass
        fun setupClass() {
//            createSchema(connectToDb())
        }
    }

    @Test
    fun testSaveMinimalSurvey() {
        val db = connectToDb()
        val repo = SurveyRepository(db)
        val survey = Survey(
            companyName = "Zenmo",
            project = "Project",
            personName = "John Doe",
            email = "john@example.com",
            gridConnections = emptyList(),
            transport = Transport(
                hasVehicles = false,
                numDailyCarCommuters = null,
                cars = Cars(
                    numCars = null,
                    numElectricCars = 0,
                    numChargePoints = 0,
                    powerPerChargePointKw = 0f,
                    annualTravelDistancePerCarKm = 0,
                    numPlannedElectricCars = 0,
                ),
                trucks = Trucks(
                    numTrucks = null,
                    numElectricTrucks = 0,
                    numChargePoints = 0,
                    powerPerChargePointKw = 0f,
                    annualTravelDistancePerTruckKm = 0,
                    numPlannedElectricTrucks = 0,
                ),
                vans = Vans(
                    numVans = null,
                    numElectricVans = 0,
                    numChargePoints = 0,
                    powerPerChargePointKw = 0f,
                    annualTravelDistancePerVanKm = 0,
                    numPlannedElectricVans = 0,
                ),
            )
        )
        repo.save(survey)
    }

    @Test
    fun testSaveWithGridConnections() {
        val db = connectToDb()
        val repo = SurveyRepository(db)
        val survey = Survey(
            companyName = "Zenmo",
            project = "Project",
            personName = "John Doe",
            email = "john@example.com",
            transport = Transport(
                hasVehicles = false,
                numDailyCarCommuters = null,
                cars = Cars(
                    numCars = null,
                    numElectricCars = 0,
                    numChargePoints = 0,
                    powerPerChargePointKw = 0f,
                    annualTravelDistancePerCarKm = 0,
                    numPlannedElectricCars = 0,
                ),
                trucks = Trucks(
                    numTrucks = null,
                    numElectricTrucks = 0,
                    numChargePoints = 0,
                    powerPerChargePointKw = 0f,
                    annualTravelDistancePerTruckKm = 0,
                    numPlannedElectricTrucks = 0,
                ),
                vans = Vans(
                    numVans = null,
                    numElectricVans = 0,
                    numChargePoints = 0,
                    powerPerChargePointKw = 0f,
                    annualTravelDistancePerVanKm = 0,
                    numPlannedElectricVans = 0,
                ),
            ),
            gridConnections = listOf(
                SurveyGridConnection(
                    address = Address(
                        street = "Kerkstraat",
                        houseNumber = 1,
                        houseLetter = "A",
                        houseNumberSuffix = "100",
                        postalCode = "1234AB",
                        city = "Amsterdam",
                    ),
                    electricity = Electricity(
                        annualElectricityDemandKwh = 1000,
                        annualElectricityProductionKwh = 2000,
                        ean = "123456789012345678",
                        quarterHourlyValuesFiles = emptyList(),
                        grootverbruik = CompanyGrootverbruik(
                            contractedConnectionDemandCapacityKw = 100,
                            contractedConnectionSupplyCapacityKw = 200,
                        ),
                        kleinverbruik = CompanyKleinverbruik(
                            connectionCapacity = KleinverbruikElectricityConnectionCapacity.`3x63A`,
                            consumptionProfile = KleinverbruikElectricityConsumptionProfile.TWO,
                        ),
                    ),
                    supply = Supply(
                        hasSupply = true,
                        pvInstalledKwp = 100,
                        pvOrientation = PVOrientation.SOUTH,
                        pvPlanned = true,
                        pvPlannedCapacityKwp = 200,
                        pvPlannedOrientation = PVOrientation.EAST_WEST,
                        pvPlannedYear = 2022,
                        windInstalledKw = 300f,
                        otherSupply = "Other supply",
                    ),
                    naturalGas = NaturalGas(
                        ean = "123456789012345678",
                        hasConnection = true,
                        annualDemandM3 = 3500,
                        hourlyValuesFile = emptyList(),
                        percentageUsedForHeating = 50,
                    ),
                    heat = Heat(
                        heatingTypes = listOf(HeatingType.GAS_BOILER, HeatingType.DISTRICT_HEATING),
                        sumGasBoilerKw = 28.8f,
                        sumHeatPumpKw = 0f,
                        sumHybridHeatPumpElectricKw = 0f,
                        annualDistrictHeatingDemandGj = 300f,
                        localHeatExchangeDescription = "Local heat exchange description",
                        hasUnusedResidualHeat = false,
                    ),
                    storage = Storage(
                        hasBattery = false,
                        batteryCapacityKwh = 0f,
                        batteryPowerKw = 0f,
                        batterySchedule = "Battery schedule",
                        hasPlannedBattery = true,
                        plannedBatteryCapacityKwh = 100f,
                        plannedBatteryPowerKw = 10f,
                        plannedBatterySchedule = "Planned battery schedule",
                    ),
                    mainConsumptionProcess = "Main consumption process",
                    electrificationPlans = "Electrification plans",
                    consumptionFlexibility = "Consumption flexibility",
                )
            )
        )
        repo.save(survey)
    }
}