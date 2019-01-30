package com.jzue.study.kafka.provider;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jzue.study.kafka.beans.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * @Author: junzexue
 * @Date: 2018/11/15 下午4:40
 * @Description:
 **/
@Slf4j
@Component
public class KafkaSender {



    public static String  getTestJson(){
        String realDataJson ="{\n" +
                "\t\"vin\": \"botaivehicle01\",\n" +
                "\t\"reportDataItems\": {\n" +
                "\t\t\"128\": {\n" +
                "\t\t\t\"vehicleStatus\": 1,\n" +
                "\t\t\t\"averageSpeed\": 219.9,\n" +
                "\t\t\t\"enduranceMileage\": 999.9,\n" +
                "\t\t\t\"pressureOfFrontTireRightWheel\": 4.0,\n" +
                "\t\t\t\"pressureOfFrontTireLeftWheel\": 4.0,\n" +
                "\t\t\t\"pressureOfBackTireRightWheel\": 4.0,\n" +
                "\t\t\t\"pressureOfBackTireLeftWheel\": 4.0,\n" +
                "\t\t\t\"gpsHeightAboveSea\": 9000.0,\n" +
                "\t\t\t\"gpsSatelliteCount\": 253,\n" +
                "\t\t\t\"gpsAzimuth\": 360\n" +
                "\t\t},\n" +
                "\t\t\"1\": {\n" +
                "\t\t\t\"carStatus\": 1,\n" +
                "\t\t\t\"chargeStatus\": 4,\n" +
                "\t\t\t\"runMode\": 2,\n" +
                "\t\t\t\"vehicleSpeed\": 219.9,\n" +
                "\t\t\t\"accumulatedMileage\": 999999.9,\n" +
                "\t\t\t\"totalVoltage\": 999.9,\n" +
                "\t\t\t\"totalCurrent\": -999.9,\n" +
                "\t\t\t\"soc\": 0.99,\n" +
                "\t\t\t\"dcDcStatus\": 1,\n" +
                "\t\t\t\"gearStatus\": 61,\n" +
                "\t\t\t\"insulationResistance\": 60000,\n" +
                "\t\t\t\"acceleratedPedalStrokeValue\": 0.99,\n" +
                "\t\t\t\"brakePedalStatus\": 0.99\n" +
                "\t\t},\n" +
                "\t\t\"2\": {\n" +
                "\t\t\t\"motorData\": [{\n" +
                "\t\t\t\t\"motorSerialNumber\": 251,\n" +
                "\t\t\t\t\"motorStatus\": 1,\n" +
                "\t\t\t\t\"motorControllerTemperature\": 210,\n" +
                "\t\t\t\t\"motorRotationSpeed\": 45531,\n" +
                "\t\t\t\t\"motorTorque\": 4553.1,\n" +
                "\t\t\t\t\"motorTemperature\": 210,\n" +
                "\t\t\t\t\"motorControllerInputVoltage\": 5999.9,\n" +
                "\t\t\t\t\"motorControllerDirectCurrent\": 999.9\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"motorSerialNumber\": 252,\n" +
                "\t\t\t\t\"motorStatus\": 2,\n" +
                "\t\t\t\t\"motorControllerTemperature\": 210,\n" +
                "\t\t\t\t\"motorRotationSpeed\": 45531,\n" +
                "\t\t\t\t\"motorTorque\": 4553.1,\n" +
                "\t\t\t\t\"motorTemperature\": 210,\n" +
                "\t\t\t\t\"motorControllerInputVoltage\": 6000.0,\n" +
                "\t\t\t\t\"motorControllerDirectCurrent\": -1000.0\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"motorSerialNumber\": 253,\n" +
                "\t\t\t\t\"motorStatus\": 3,\n" +
                "\t\t\t\t\"motorControllerTemperature\": 210,\n" +
                "\t\t\t\t\"motorRotationSpeed\": 45531,\n" +
                "\t\t\t\t\"motorTorque\": 4553.1,\n" +
                "\t\t\t\t\"motorTemperature\": 210,\n" +
                "\t\t\t\t\"motorControllerInputVoltage\": 6000.0,\n" +
                "\t\t\t\t\"motorControllerDirectCurrent\": 1000.0\n" +
                "\t\t\t}]\n" +
                "\t\t},\n" +
                "\t\t\"130\": {\n" +
                "\t\t\t\"leftDoorStatus\": 0,\n" +
                "\t\t\t\"leftFrontWindowStatus\": 0.99,\n" +
                "\t\t\t\"leftBackWindowStatus\": 0.99,\n" +
                "\t\t\t\"rightFrontWindowStatus\": 0.99,\n" +
                "\t\t\t\"rightBackWindowStatus\": 0.99,\n" +
                "\t\t\t\"airConditionerRunMode\": 1,\n" +
                "\t\t\t\"airConditionerTemperature\": 32,\n" +
                "\t\t\t\"engineStatus\": 0,\n" +
                "\t\t\t\"skylightStatus\": 0,\n" +
                "\t\t\t\"dangerAlarmLampStatus\": 0,\n" +
                "\t\t\t\"wistleBlowStatus\": 0,\n" +
                "\t\t\t\"trunkStatus\": 0,\n" +
                "\t\t\t\"slaveSeatHeatStatus\": 0,\n" +
                "\t\t\t\"masterSeatHeatStatus\": 0,\n" +
                "\t\t\t\"batteryHeatStatus\": 0,\n" +
                "\t\t\t\"currentSoC\": 0.99,\n" +
                "\t\t\t\"enduranceMileage\": 999.9,\n" +
                "\t\t\t\"chargeStatus\": 0\n" +
                "\t\t},\n" +
                "\t\t\"3\": {\n" +
                "\t\t\t\"fuelBatteryVoltage\": 2000.0,\n" +
                "\t\t\t\"fuelBatteryCurrent\": 2000.0,\n" +
                "\t\t\t\"batteryFuelConsumptionRate\": 599.05,\n" +
                "\t\t\t\"probeTemperatures\": [-40, 199, 200],\n" +
                "\t\t\t\"hydrogenSystemMaxTemperature\": 200.0,\n" +
                "\t\t\t\"hydrogenSystemMaxTemperatureProbe\": 4,\n" +
                "\t\t\t\"hydrogenMaxDensity\": 50000,\n" +
                "\t\t\t\"hydrogenMaxDensitySensor\": 252,\n" +
                "\t\t\t\"hydrogenMaxPressure\": 100.1,\n" +
                "\t\t\t\"hydrogenMaxPressureSensor\": 252,\n" +
                "\t\t\t\"highDcDcStatus\": 1\n" +
                "\t\t},\n" +
                "\t\t\"131\": {\n" +
                "\t\t\t\"batteryStatus\": 0,\n" +
                "\t\t\t\"totalMileage\": 999.9,\n" +
                "\t\t\t\"maintainMileage\": 999.9,\n" +
                "\t\t\t\"maintainTime\": 100,\n" +
                "\t\t\t\"remainingElectricity\": 0.99,\n" +
                "\t\t\t\"enduranceMileage\": 999.9,\n" +
                "\t\t\t\"network\": 1,\n" +
                "\t\t\t\"signalIntensity\": 100,\n" +
                "\t\t\t\"instantSpeed\": 219.9,\n" +
                "\t\t\t\"currentSpeed\": 219.9,\n" +
                "\t\t\t\"averageSpeed\": 219.9,\n" +
                "\t\t\t\"electricityConsumption\": 10,\n" +
                "\t\t\t\"mileage\": 10,\n" +
                "\t\t\t\"rapidAcceleration\": 10,\n" +
                "\t\t\t\"rapidDeceleration\": 10,\n" +
                "\t\t\t\"rapidTurn\": 10,\n" +
                "\t\t\t\"chargeStatus\": 2\n" +
                "\t\t},\n" +
                "\t\t\"4\": {\n" +
                "\t\t\t\"engineStatus\": 6,\n" +
                "\t\t\t\"rotationSpeed\": 50000,\n" +
                "\t\t\t\"engineFuelConsumptionRate\": 200.3\n" +
                "\t\t},\n" +
                "\t\t\"5\": {\n" +
                "\t\t\t\"positionStatus\": 6,\n" +
                "\t\t\t\"longitude\": 121.622643,\n" +
                "\t\t\t\"latitude\": 31.083729\n" +
                "\t\t},\n" +
                "\t\t\"6\": {\n" +
                "\t\t\t\"maxVoltageBatterySubsystem\": 250,\n" +
                "\t\t\t\"maxVoltageBattery\": 250,\n" +
                "\t\t\t\"batteryMaxVoltage\": 14.999,\n" +
                "\t\t\t\"minVoltageBatterySubsystem\": 250,\n" +
                "\t\t\t\"minVoltageBattery\": 250,\n" +
                "\t\t\t\"batteryMinVoltage\": 14.999,\n" +
                "\t\t\t\"maxTemperatureSubsystem\": 250,\n" +
                "\t\t\t\"maxTemperatureProbe\": 250,\n" +
                "\t\t\t\"maxTemperature\": 210,\n" +
                "\t\t\t\"minTemperatureSubsystem\": 250,\n" +
                "\t\t\t\"minTemperatureProbe\": 250,\n" +
                "\t\t\t\"minTemperature\": -40\n" +
                "\t\t},\n" +
                "\t\t\"7\": {\n" +
                "\t\t\t\"vin\": \"botaivehicle01\",\n" +
                "\t\t\t\"maxAlarmLevel\": 3,\n" +
                "\t\t\t\"generalAlarmFlag\": -268435455,\n" +
                "\t\t\t\"chargeableStorageErrors\": [10, 11, 12],\n" +
                "\t\t\t\"motorErrors\": [20, 21, 22],\n" +
                "\t\t\t\"engineErrors\": [30, 31, 32],\n" +
                "\t\t\t\"otherErrors\": [40, 41, 42]\n" +
                "\t\t},\n" +
                "\t\t\"8\": {\n" +
                "\t\t\t\"rechargeableStorageDeviceVoltageData\": [{\n" +
                "\t\t\t\t\"rechargeableStorageSubsystem\": 1,\n" +
                "\t\t\t\t\"rechargeableStorageDeviceVoltage\": 999.9,\n" +
                "\t\t\t\t\"rechargeableStorageDeviceCurrent\": 999.9,\n" +
                "\t\t\t\t\"batteryCount\": 65531,\n" +
                "\t\t\t\t\"batteryStartNumInThisFrame\": 65531,\n" +
                "\t\t\t\t\"batteryVoltages\": [58.999, 59.999, 0.0]\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"rechargeableStorageSubsystem\": 1,\n" +
                "\t\t\t\t\"rechargeableStorageDeviceVoltage\": 999.9,\n" +
                "\t\t\t\t\"rechargeableStorageDeviceCurrent\": 999.9,\n" +
                "\t\t\t\t\"batteryCount\": 65531,\n" +
                "\t\t\t\t\"batteryStartNumInThisFrame\": 65531,\n" +
                "\t\t\t\t\"batteryVoltages\": [58.999, 59.999, 0.0]\n" +
                "\t\t\t}]\n" +
                "\t\t},\n" +
                "\t\t\"9\": {\n" +
                "\t\t\t\"rechargeableStorageDeviceTemperatureData\": [{\n" +
                "\t\t\t\t\"rechargeableSubsystem\": 1,\n" +
                "\t\t\t\t\"rechargeableProbeTemperatures\": [-40, -39, -38]\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"rechargeableSubsystem\": 2,\n" +
                "\t\t\t\t\"rechargeableProbeTemperatures\": [208, 209, 210]\n" +
                "\t\t\t}]\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"vehicleReportTimestamp\": 1536569685000\n" +
                "}";
        return realDataJson;
    }

    public static  String getTest( ){
        return null;
        //return "011301160d0814010100038001564e4e3030303030303030303030303031004f1301160d08141001000102001403001e040f0568747470733a2f2f636e2e62696e672e636f6d061f9007564573696f08564573696f09ff0a00280b00320cf00d060e6b6b6b6b6b7474740f1f9a1002";
        //return "011301080f340e0102000380fe564e4e3030303030303030303030303031004d1301080f340e01313536313131313232323200616c69636500616c696365000000ff6464641f9041534446414141414142424242424654503a2f2f3230302e3130302e3230302e31303000ea60";
//        return "01120C14121302010200010901564E4E3030303030303030303030303031000F120C1412130ACD0101010005010000";
        //return "01120C14121302010100010201564E4E3030303030303030303030303031001A120C1412130201636363630514010101010101010202040001";
    }

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    public void send(String str){


        log.info("+++++++++++++++++++++  message = {}", str);
        kafkaTemplate.send("5ecd841bda214500bc1c7e38fffd8362__ivc_botai_command_up", str);
    }
}
