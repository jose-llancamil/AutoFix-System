-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: db-autofix
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bonuses`
--

DROP TABLE IF EXISTS `bonuses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bonuses` (
  `bonus_id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(10,2) NOT NULL,
  `brand` varchar(255) COLLATE utf8mb3_bin DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb3_bin NOT NULL,
  `vehicle_id` bigint DEFAULT NULL,
  PRIMARY KEY (`bonus_id`),
  KEY `FKptx4kh90lu7t4y1wnx23hfac1` (`vehicle_id`),
  CONSTRAINT `FKptx4kh90lu7t4y1wnx23hfac1` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles` (`vehicle_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bonuses`
--

LOCK TABLES `bonuses` WRITE;
/*!40000 ALTER TABLE `bonuses` DISABLE KEYS */;
INSERT INTO `bonuses` VALUES (1,7000.00,'Toyota','Bono Toyota por promoción',1),(2,5000.00,'Ford','Bono Ford por promoción',2),(3,3000.00,'Hyundai','Bono Hyundai por promoción',3),(4,4000.00,'Honda','Bono Honda por promoción',4);
/*!40000 ALTER TABLE `bonuses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `charges`
--

DROP TABLE IF EXISTS `charges`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `charges` (
  `charge_id` bigint NOT NULL AUTO_INCREMENT,
  `amount` double NOT NULL,
  `applicable_type` varchar(255) COLLATE utf8mb3_bin DEFAULT NULL,
  `charge_type` enum('MILEAGE','VEHICLE_AGE','PICKUP_DELAY') COLLATE utf8mb3_bin NOT NULL,
  `description` varchar(255) COLLATE utf8mb3_bin NOT NULL,
  PRIMARY KEY (`charge_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `charges`
--

LOCK TABLES `charges` WRITE;
/*!40000 ALTER TABLE `charges` DISABLE KEYS */;
INSERT INTO `charges` VALUES (1,30000,'Sedan','MILEAGE','Recargo por kilometraje alto'),(2,25000,'SUV','VEHICLE_AGE','Recargo por antigüedad del vehículo'),(3,20000,'Hatchback','PICKUP_DELAY','Recargo por retraso en la recogida');
/*!40000 ALTER TABLE `charges` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `discounts`
--

DROP TABLE IF EXISTS `discounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `discounts` (
  `discount_id` bigint NOT NULL AUTO_INCREMENT,
  `amount` double NOT NULL,
  `applicable_brand` varchar(255) COLLATE utf8mb3_bin DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb3_bin NOT NULL,
  `discount_type` enum('NUM_REPAIRS','DAY_OF_WEEK','LOYALTY','BONUS') COLLATE utf8mb3_bin NOT NULL,
  PRIMARY KEY (`discount_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discounts`
--

LOCK TABLES `discounts` WRITE;
/*!40000 ALTER TABLE `discounts` DISABLE KEYS */;
INSERT INTO `discounts` VALUES (1,5000,'Toyota','Descuento por pronto pago','NUM_REPAIRS'),(2,10000,'Ford','Descuento por volumen','NUM_REPAIRS'),(3,7000,'Hyundai','Descuento por lealtad','LOYALTY');
/*!40000 ALTER TABLE `discounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `repair_types`
--

DROP TABLE IF EXISTS `repair_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `repair_types` (
  `repair_type_id` bigint NOT NULL AUTO_INCREMENT,
  `base_cost_diesel` decimal(38,2) NOT NULL,
  `base_cost_electric` decimal(38,2) NOT NULL,
  `base_cost_gasoline` decimal(38,2) NOT NULL,
  `base_cost_hybrid` decimal(38,2) NOT NULL,
  `description` varchar(255) COLLATE utf8mb3_bin NOT NULL,
  PRIMARY KEY (`repair_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `repair_types`
--

LOCK TABLES `repair_types` WRITE;
/*!40000 ALTER TABLE `repair_types` DISABLE KEYS */;
INSERT INTO `repair_types` VALUES (1,120000.00,220000.00,120000.00,180000.00,'Cambio de aceite'),(2,130000.00,230000.00,130000.00,190000.00,'Cambio de llantas'),(3,450000.00,800000.00,350000.00,700000.00,'Revisión de motor'),(4,210000.00,300000.00,210000.00,300000.00,'Revisión de frenos'),(5,150000.00,250000.00,150000.00,200000.00,'Ajuste de suspensión'),(6,120000.00,0.00,100000.00,450000.00,'Cambio de batería'),(7,100000.00,100000.00,100000.00,100000.00,'Pintura y detalle'),(8,180000.00,250000.00,180000.00,210000.00,'Reemplazo de parabrisas'),(9,150000.00,180000.00,150000.00,180000.00,'Revisión del sistema eléctrico'),(10,140000.00,0.00,130000.00,220000.00,'Cambio de correa de distribución'),(11,80000.00,80000.00,80000.00,80000.00,'Revisión general');
/*!40000 ALTER TABLE `repair_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `repairs`
--

DROP TABLE IF EXISTS `repairs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `repairs` (
  `repair_id` bigint NOT NULL AUTO_INCREMENT,
  `customer_pickup_date` date DEFAULT NULL,
  `customer_pickup_time` time(6) DEFAULT NULL,
  `entry_date` date NOT NULL,
  `entry_time` time(6) NOT NULL,
  `exit_date` date DEFAULT NULL,
  `exit_time` time(6) DEFAULT NULL,
  `repair_cost` decimal(38,2) NOT NULL,
  `status` varchar(255) COLLATE utf8mb3_bin DEFAULT NULL,
  `repair_type_id` bigint NOT NULL,
  `vehicle_id` bigint NOT NULL,
  PRIMARY KEY (`repair_id`),
  KEY `FKfgoovhs84b9siepriy83nyydq` (`repair_type_id`),
  KEY `FKr8rwhlbv43kxbn4j93hkul7ax` (`vehicle_id`),
  CONSTRAINT `FKfgoovhs84b9siepriy83nyydq` FOREIGN KEY (`repair_type_id`) REFERENCES `repair_types` (`repair_type_id`),
  CONSTRAINT `FKr8rwhlbv43kxbn4j93hkul7ax` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles` (`vehicle_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `repairs`
--

LOCK TABLES `repairs` WRITE;
/*!40000 ALTER TABLE `repairs` DISABLE KEYS */;
INSERT INTO `repairs` VALUES (1,'2023-01-16','10:00:00.000000','2023-01-15','08:00:00.000000','2023-01-15','10:00:00.000000',120000.00,'Completed',1,1),(2,'2023-02-21','11:00:00.000000','2023-02-20','09:00:00.000000','2023-02-20','11:00:00.000000',130000.00,'Completed',2,2),(3,'2023-03-26','12:00:00.000000','2023-03-25','10:00:00.000000','2023-03-25','12:00:00.000000',700000.00,'In Progress',3,3),(4,'2023-04-02','12:00:00.000000','2023-04-01','10:00:00.000000','2023-04-01','12:00:00.000000',210000.00,'Completed',4,1),(5,'2023-04-11','12:00:00.000000','2023-04-10','10:00:00.000000','2023-04-10','12:00:00.000000',150000.00,'Completed',5,1);
/*!40000 ALTER TABLE `repairs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicles`
--

DROP TABLE IF EXISTS `vehicles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicles` (
  `vehicle_id` bigint NOT NULL AUTO_INCREMENT,
  `brand` varchar(255) COLLATE utf8mb3_bin NOT NULL,
  `engine_type` varchar(255) COLLATE utf8mb3_bin NOT NULL,
  `license_plate_number` varchar(255) COLLATE utf8mb3_bin NOT NULL,
  `manufacture_year` int NOT NULL,
  `mileage` int NOT NULL,
  `model` varchar(255) COLLATE utf8mb3_bin NOT NULL,
  `seat_count` int NOT NULL,
  `type` varchar(255) COLLATE utf8mb3_bin NOT NULL,
  PRIMARY KEY (`vehicle_id`),
  UNIQUE KEY `UK_g7mbknasd4vv22tsr4jfj4qbf` (`license_plate_number`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicles`
--

LOCK TABLES `vehicles` WRITE;
/*!40000 ALTER TABLE `vehicles` DISABLE KEYS */;
INSERT INTO `vehicles` VALUES (1,'Toyota','Gasoline','ABC123',2020,12000,'Corolla',5,'Sedan'),(2,'Ford','Diesel','DEF456',2019,5000,'Fiesta',5,'Hatchback'),(3,'Hyundai','Hybrid','GHI789',2021,20000,'Sonata',5,'Sedan'),(4,'Honda','Electric','JKL012',2018,15000,'Civic',5,'Sedan'),(5,'Toyota','Gasoline','MNO345',2017,30000,'Camry',5,'Sedan');
/*!40000 ALTER TABLE `vehicles` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-04-27 21:25:51
