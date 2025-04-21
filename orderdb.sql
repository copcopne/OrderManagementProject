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
  `UserID` int DEFAULT NULL,
  `FullName` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `Address` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`CustomerID`),
  KEY `UserID` (`UserID`),
  CONSTRAINT `customers_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (1,6,'Nguyễn Văn A','123 Lê Lợi, Hà Nội'),(2,7,'Trần Thị B','456 Nguyễn Huệ, TP.HCM'),(3,8,'Lê Văn C','789 Hai Bà Trưng, Đà Nẵng'),(4,9,'Phạm Thị D','101 Trường Chinh, Hải Phòng'),(5,10,'Đặng Quốc E','202 Cầu Giấy, Hà Nội'),(6,NULL,'Hoàng An','303 Phạm Văn Đồng, Cần Thơ'),(7,NULL,'Võ Minh','404 Quang Trung, Nha Trang'),(8,NULL,'Đinh Huy','505 Nguyễn Văn Linh, Huế'),(9,NULL,'Bùi Hương','606 Lý Tự Trọng, Vũng Tàu'),(10,NULL,'Hồ Hải','707 Hoàng Hoa Thám, Đà Lạt');
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
  `UnitPrice` float NOT NULL,
  `Quanity` int NOT NULL,
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
INSERT INTO `orderdetails` VALUES (1,'P001',1500,1,0),(1,'P002',1200,2,50),(2,'P003',1100,1,0),(2,'P004',2000,1,100),(3,'P005',250,3,10),(3,'P006',80,2,5),(4,'P007',60,1,0),(4,'P008',350,2,15),(5,'P009',180,1,0),(5,'P010',150,2,10),(6,'P011',130,1,5),(6,'P012',90,3,10),(7,'P013',400,1,0),(7,'P014',300,2,20),(8,'P015',120,1,0),(8,'P016',200,2,30),(9,'P017',400,1,50),(9,'P018',80,3,10),(10,'P019',35,5,5),(10,'P020',500,1,0);
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
  `SKU` varchar(8) NOT NULL,
  `ProductName` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `StockQuanity` int NOT NULL,
  `UnitPrice` float NOT NULL,
  `MinStockLevel` int NOT NULL DEFAULT '10',
  PRIMARY KEY (`SKU`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES ('P001','Laptop Dell',50,1500,5),('P002','iPhone 15',30,1200,3),('P003','Samsung Galaxy S24',40,1100,4),('P004','MacBook Pro',20,2000,2),('P005','AirPods Pro',100,250,10),('P006','Bàn phím cơ',80,80,5),('P007','Chuột gaming',75,60,6),('P008','Màn hình LG 27\"',25,350,3),('P009','Tai nghe Sony',50,180,5),('P010','Ổ cứng SSD 1TB',60,150,7),('P011','Router WiFi 6',40,130,4),('P012','Loa Bluetooth JBL',55,90,6),('P013','Điện thoại Oppo',35,400,3),('P014','Đồng hồ thông minh',45,300,4),('P015','Bộ phát WiFi 4G',50,120,6),('P016','Camera an ninh',30,200,3),('P017','Máy in Canon',20,400,2),('P018','Micro thu âm',70,80,6),('P019','Bộ sạc nhanh 65W',90,35,10),('P020','Máy chiếu mini',15,500,2);
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
  `Username` varchar(50) NOT NULL,
  `Password` varchar(255) NOT NULL,
  `IsStaff` tinyint(1) NOT NULL,
  PRIMARY KEY (`UserID`),
  UNIQUE KEY `Username` (`Username`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'staff1','hashed_password1',1),(2,'staff2','hashed_password2',1),(3,'staff3','hashed_password3',1),(4,'staff4','hashed_password4',1),(5,'staff5','hashed_password5',1),(6,'customer1','hashed_password6',0),(7,'customer2','hashed_password7',0),(8,'customer3','hashed_password8',0),(9,'customer4','hashed_password9',0),(10,'customer5','hashed_password10',0),(11,'customer6','hashed_password11',0),(12,'customer7','hashed_password12',0),(13,'customer8','hashed_password13',0),(14,'customer9','hashed_password14',0),(15,'customer10','hashed_password15',0);
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

-- Dump completed on 2025-03-24 10:01:33
