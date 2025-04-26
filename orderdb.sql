-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: orderdb
-- ------------------------------------------------------
-- Server version	8.0.41

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
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `CustomerID` int NOT NULL AUTO_INCREMENT,
  `FullName` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `Address` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`CustomerID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (1,'Nguyễn Văn A','123 Lê Lợi, Hà Nội'),(2,'Trần Thị B','456 Nguyễn Huệ, TP.HCM'),(3,'Lê Văn C','789 Hai Bà Trưng, Đà Nẵng'),(4,'Phạm Thị D','101 Trường Chinh, Hải Phòng'),(5,'Đặng Quốc E','202 Cầu Giấy, Hà Nội'),(6,'Hoàng An','303 Phạm Văn Đồng, Cần Thơ'),(7,'Võ Minh','404 Quang Trung, Nha Trang'),(8,'Đinh Huy','505 Nguyễn Văn Linh, Huế'),(9,'Bùi Hương','606 Lý Tự Trọng, Vũng Tàu'),(10,'Hồ Hải','707 Hoàng Hoa Thám, Đà Lạt');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orderdetails`
--

DROP TABLE IF EXISTS `orderdetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orderdetails` (
  `OrderID` int NOT NULL,
  `SKU` varchar(8) NOT NULL,
  `Quantity` int NOT NULL,
  `Discount` float DEFAULT NULL,
  PRIMARY KEY (`OrderID`,`SKU`),
  KEY `SKU` (`SKU`),
  CONSTRAINT `orderdetails_ibfk_1` FOREIGN KEY (`OrderID`) REFERENCES `orders` (`OrderID`),
  CONSTRAINT `orderdetails_ibfk_2` FOREIGN KEY (`SKU`) REFERENCES `products` (`SKU`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orderdetails`
--

LOCK TABLES `orderdetails` WRITE;
/*!40000 ALTER TABLE `orderdetails` DISABLE KEYS */;
INSERT INTO `orderdetails` VALUES (1,'P001',1,0),(1,'P002',20,0.5),(2,'P003',1,0),(2,'P004',1,0.1),(3,'P005',3,0.2),(3,'P006',2,0.5),(4,'P007',1,0),(4,'P008',2,0.15),(5,'P009',1,0),(5,'P010',2,0.1),(6,'P011',1,0.05),(6,'P012',3,0.1),(7,'P013',1,0),(7,'P014',2,0.2),(8,'P015',1,0),(8,'P016',2,0.2),(9,'P017',1,0.5),(9,'P018',3,0.1),(10,'P019',5,0),(10,'P020',1,0);
/*!40000 ALTER TABLE `orderdetails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `OrderID` int NOT NULL AUTO_INCREMENT,
  `CustomerID` int NOT NULL,
  `OrderDate` datetime NOT NULL,
  `ShipAddress` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `OrderStatus` enum('Đang xử lý','Đang giao','Đã giao') NOT NULL,
  PRIMARY KEY (`OrderID`),
  KEY `CustomerID` (`CustomerID`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`CustomerID`) REFERENCES `customers` (`CustomerID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,1,'2025-03-01 10:30:00','123 Lê Lợi, Hà Nội','Đã giao'),(2,2,'2025-03-02 14:00:00','456 Nguyễn Huệ, TP.HCM','Đã giao'),(3,3,'2025-03-03 16:15:00','789 Hai Bà Trưng, Đà Nẵng','Đang giao'),(4,4,'2025-03-04 11:45:00','101 Trường Chinh, Hải Phòng','Đang xử lý'),(5,5,'2025-03-05 09:30:00','202 Cầu Giấy, Hà Nội','Đang xử lý'),(6,6,'2025-03-06 13:20:00','303 Phạm Văn Đồng, Cần Thơ','Đã giao'),(7,7,'2025-03-07 15:50:00','404 Quang Trung, Nha Trang','Đang giao'),(8,8,'2025-03-08 17:30:00','505 Nguyễn Văn Linh, Huế','Đang xử lý'),(9,9,'2025-03-09 19:45:00','606 Lý Tự Trọng, Vũng Tàu','Đang xử lý'),(10,10,'2025-03-10 20:10:00','707 Hoàng Hoa Thám, Đà Lạt','Đã giao');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `ProductID` int NOT NULL AUTO_INCREMENT,
  `SKU` varchar(8) NOT NULL,
  `ProductName` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `StockQuantity` int NOT NULL,
  `UnitPrice` float NOT NULL,
  `MinStockLevel` int NOT NULL DEFAULT '10',
  PRIMARY KEY (`ProductID`),
  UNIQUE KEY `SKU` (`SKU`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'P001','Laptop Dell',50,1500,5),(2,'P002','iPhone 15',2,1200,3),(3,'P003','Samsung Galaxy S24',40,1100,4),(4,'P004','MacBook Pro',8,2000,10),(5,'P005','AirPods Pro',100,250,10),(6,'P006','Bàn phím cơ',80,80,5),(7,'P007','Chuột gaming',75,60,6),(8,'P008','Màn hình LG 27\"',25,350,3),(9,'P009','Tai nghe Sony',50,180,5),(10,'P010','Ổ cứng SSD 1TB',60,150,7),(11,'P011','Router WiFi 6',40,130,4),(12,'P012','Loa Bluetooth JBL',55,90,6),(13,'P013','Điện thoại Oppo',35,400,3),(14,'P014','Đồng hồ thông minh',10,300,4),(15,'P015','Bộ phát WiFi 4G',20,120,6),(16,'P016','Camera an ninh',30,200,3),(17,'P017','Máy in Canon',20,400,2),(18,'P018','Micro thu âm',70,80,6),(19,'P019','Bộ sạc nhanh 65W',90,35,10),(20,'P020','Máy chiếu mini',15,500,2);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `UserID` int NOT NULL AUTO_INCREMENT,
  `FullName` varchar(50) NOT NULL,
  `Username` varchar(50) NOT NULL,
  `Password` varchar(255) NOT NULL,
  `IsAdmin` tinyint(1) NOT NULL,
  PRIMARY KEY (`UserID`),
  UNIQUE KEY `Username` (`Username`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','Admin','XVpwoteIeYNNs1g/jSdVguzgbq542w1ADdpaCJV8YHQ=',1),(2,'staff1','staff1','XVpwoteIeYNNs1g/jSdVguzgbq542w1ADdpaCJV8YHQ=',0),(3,'staff2','Nhân Viên A','XVpwoteIeYNNs1g/jSdVguzgbq542w1ADdpaCJV8YHQ=',0),(4,'staff3','Nhân Viên B','XVpwoteIeYNNs1g/jSdVguzgbq542w1ADdpaCJV8YHQ=',0);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-26 18:00:31
