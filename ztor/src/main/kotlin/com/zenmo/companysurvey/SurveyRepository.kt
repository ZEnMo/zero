package com.zenmo.companysurvey

import com.zenmo.companysurvey.dto.Survey
import com.zenmo.companysurvey.table.CompanySurveyGridConnectionTable
import com.zenmo.companysurvey.table.CompanySurveyTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class SurveyRepository(
    private val db: Database
) {
    fun save(survey: Survey): UUID {
        val surveyId = UUID.randomUUID()

        transaction(db) {
            CompanySurveyTable.insert {
                it[id] = surveyId
                it[project] = survey.project
                it[companyName] = survey.companyName
                it[personName] = survey.personName
                it[email] = survey.email

                it[hasVehicles] = survey.transport.hasVehicles
                it[numDailyCarCommuters] = survey.transport.numDailyCarCommuters?.toUInt()

                it[numTrucks] = survey.transport.trucks.numTrucks?.toUInt()
                it[numElectricTrucks] = survey.transport.trucks.numElectricTrucks?.toUInt()
                it[numTruckChargePoints] = survey.transport.trucks.numChargePoints?.toUInt()
                it[powerPerTruckChargePointKw] = survey.transport.trucks.powerPerChargePointKw
                it[annualTravelDistancePerTruckKm] = survey.transport.trucks.annualTravelDistancePerTruckKm?.toUInt()
                it[numPlannedElectricTrucks] = survey.transport.trucks.numPlannedElectricTrucks?.toUInt()

                it[numVans] = survey.transport.vans.numVans?.toUInt()
                it[numElectricVans] = survey.transport.vans.numElectricVans?.toUInt()
                it[numVanChargePoints] = survey.transport.vans.numChargePoints?.toUInt()
                it[powerPerVanChargePointKw] = survey.transport.vans.powerPerChargePointKw
                it[annualTravelDistancePerVanKm] = survey.transport.vans.annualTravelDistancePerVanKm?.toUInt()
                it[numPlannedElectricVans] = survey.transport.vans.numPlannedElectricVans?.toUInt()

                it[numCars] = survey.transport.cars.numCars?.toUInt()
                it[numElectricCars] = survey.transport.cars.numElectricCars?.toUInt()
                it[numCarChargePoints] = survey.transport.cars.numChargePoints?.toUInt()
                it[powerPerCarChargePointKw] = survey.transport.cars.powerPerChargePointKw
                it[annualTravelDistancePerCarKm] = survey.transport.cars.annualTravelDistancePerCarKm?.toUInt()
                it[numPlannedElectricCars] = survey.transport.cars.numPlannedElectricCars?.toUInt()
            }

            CompanySurveyGridConnectionTable.batchInsert(survey.gridConnections) {
                    gridConnection ->
                this[CompanySurveyGridConnectionTable.surveyId] = surveyId

                // address
                this[CompanySurveyGridConnectionTable.street] = gridConnection.address.street
                this[CompanySurveyGridConnectionTable.houseNumber] = gridConnection.address.houseNumber.toUInt()
                this[CompanySurveyGridConnectionTable.houseLetter] = gridConnection.address.houseLetter
                this[CompanySurveyGridConnectionTable.houseNumberSuffix] = gridConnection.address.houseNumberSuffix
                this[CompanySurveyGridConnectionTable.postalCode] = gridConnection.address.postalCode
                this[CompanySurveyGridConnectionTable.city] = gridConnection.address.city

                // electricity
                this[CompanySurveyGridConnectionTable.electricityEan] = gridConnection.electricity.ean
                try {
                    this[CompanySurveyGridConnectionTable.quarterHourlyElectricityObjectKey] =
                        gridConnection.electricity.quarterHourlyValuesFiles.first().remoteName
                } catch (_: NoSuchElementException) {
                    this[CompanySurveyGridConnectionTable.quarterHourlyElectricityObjectKey] = ""
                }
                this[CompanySurveyGridConnectionTable.annualElectricityDemandKwh] = gridConnection.electricity.annualElectricityDemandKwh?.toUInt()
                this[CompanySurveyGridConnectionTable.annualElectricityProductionKwh] = gridConnection.electricity.annualElectricityProductionKwh?.toUInt()
                this[CompanySurveyGridConnectionTable.kleinverbruikElectricityConnectionCapacity] = gridConnection.electricity.kleinverbruik?.connectionCapacity
                this[CompanySurveyGridConnectionTable.kleinverbuikElectricityConsumptionProfile] = gridConnection.electricity.kleinverbruik?.consumptionProfile
                this[CompanySurveyGridConnectionTable.grootverbruikContractedDemandCapacityKw] = gridConnection.electricity.grootverbruik?.contractedConnectionDemandCapacityKw?.toUInt()
                this[CompanySurveyGridConnectionTable.grootverbruikContractedSupplyCapacityKw] = gridConnection.electricity.grootverbruik?.contractedConnectionSupplyCapacityKw?.toUInt()

                // supply
                this[CompanySurveyGridConnectionTable.hasSupply] = gridConnection.supply.hasSupply
                this[CompanySurveyGridConnectionTable.pvInstalledKwp] = gridConnection.supply.pvInstalledKwp?.toUInt()
                this[CompanySurveyGridConnectionTable.pvOrientation] = gridConnection.supply.pvOrientation
                this[CompanySurveyGridConnectionTable.pvPlanned] = gridConnection.supply.pvPlanned
                this[CompanySurveyGridConnectionTable.pvPlannedCapacityKwp] = gridConnection.supply.pvPlannedCapacityKwp?.toUInt()
                this[CompanySurveyGridConnectionTable.pvPlannedYear] = gridConnection.supply.pvPlannedYear?.toUInt()
                this[CompanySurveyGridConnectionTable.windInstalledKw] = gridConnection.supply.windInstalledKw
                this[CompanySurveyGridConnectionTable.otherSupply] = gridConnection.supply.otherSupply

                // natural gas
                this[CompanySurveyGridConnectionTable.hasNaturalGasConnection] = gridConnection.naturalGas.hasConnection
                this[CompanySurveyGridConnectionTable.naturalGasEan] = gridConnection.naturalGas.ean
                this[CompanySurveyGridConnectionTable.naturalGasAnnualDemandM3] = gridConnection.naturalGas.annualDemandM3?.toUInt()
                try {
                    this[CompanySurveyGridConnectionTable.hourlyNaturalGasObjectKey] = gridConnection.naturalGas.hourlyValuesFile.first().remoteName
                } catch (_: NoSuchElementException) {
                    this[CompanySurveyGridConnectionTable.hourlyNaturalGasObjectKey] = ""
                }
                this[CompanySurveyGridConnectionTable.percentageNaturalGasForHeating] = gridConnection.naturalGas.percentageUsedForHeating?.toUInt()

                // heat
                this[CompanySurveyGridConnectionTable.heatingTypes] = gridConnection.heat.heatingTypes
                this[CompanySurveyGridConnectionTable.sumGasBoilerKw] = gridConnection.heat.sumGasBoilerKw
                this[CompanySurveyGridConnectionTable.sumHeatPumpKw] = gridConnection.heat.sumHeatPumpKw
                this[CompanySurveyGridConnectionTable.sumHybridHeatPumpElectricKw] = gridConnection.heat.sumHybridHeatPumpElectricKw
                this[CompanySurveyGridConnectionTable.annualDistrictHeatingDemandGj] = gridConnection.heat.annualDistrictHeatingDemandGj
                this[CompanySurveyGridConnectionTable.localHeatExchangeDescription] = gridConnection.heat.localHeatExchangeDescription
                this[CompanySurveyGridConnectionTable.hasUnusedResidualHeat] = gridConnection.heat.hasUnusedResidualHeat

                // storage
                this[CompanySurveyGridConnectionTable.hasBattery] = gridConnection.storage.hasBattery
                this[CompanySurveyGridConnectionTable.batteryCapacityKwh] = gridConnection.storage.batteryCapacityKwh
                this[CompanySurveyGridConnectionTable.batteryPowerKw] = gridConnection.storage.batteryPowerKw
                this[CompanySurveyGridConnectionTable.batterySchedule] = gridConnection.storage.batterySchedule
                this[CompanySurveyGridConnectionTable.hasPlannedBattery] = gridConnection.storage.hasPlannedBattery
                this[CompanySurveyGridConnectionTable.plannedBatteryCapacityKwh] = gridConnection.storage.plannedBatteryCapacityKwh
                this[CompanySurveyGridConnectionTable.plannedBatteryPowerKw] = gridConnection.storage.plannedBatteryPowerKw
                this[CompanySurveyGridConnectionTable.plannedBatterySchedule] = gridConnection.storage.plannedBatterySchedule
            }
        }

        return surveyId
    }
}