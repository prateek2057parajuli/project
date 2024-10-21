-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Oct 21, 2024 at 06:22 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `todo`
--

-- --------------------------------------------------------

--
-- Table structure for table `Task`
--

CREATE TABLE `Task` (
  `task_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `task_name` varchar(255) NOT NULL,
  `priority` varchar(20) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT current_timestamp(),
  `end_time` datetime DEFAULT NULL,
  `completed` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `Task`
--

INSERT INTO `Task` (`task_id`, `user_id`, `task_name`, `priority`, `create_time`, `end_time`, `completed`) VALUES
(1, 1, 'Task One', 'Medium', '2024-10-21 16:15:42', '2024-10-21 22:00:36', 1),
(2, 1, 'Task Two', 'Low', '2024-10-21 16:15:50', '2024-10-21 22:00:45', 1),
(3, 1, 'task 3', 'High', '2024-10-21 16:16:05', '2024-10-21 22:01:01', 1),
(4, 2, 'asdfasdf', 'High', '2024-10-21 16:17:59', '2024-10-21 22:02:57', 1),
(5, 2, 'asdfasdfasf', 'High', '2024-10-21 16:18:02', '2024-10-21 22:03:01', 0),
(6, 2, 'sdfasdfasdfasdf', 'High', '2024-10-21 16:18:05', '2024-10-21 22:03:04', 1);

-- --------------------------------------------------------

--
-- Table structure for table `Users`
--

CREATE TABLE `Users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(100) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `Users`
--

INSERT INTO `Users` (`user_id`, `username`, `password`, `email`, `created_at`) VALUES
(1, 'santoshvandari', 'Santosh1@1', 'santosh@gmail.com', '2024-10-21 16:13:18'),
(2, 'temp', 'Temporary1@', 'temp@gmail.com', '2024-10-21 16:15:23');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Task`
--
ALTER TABLE `Task`
  ADD PRIMARY KEY (`task_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `Users`
--
ALTER TABLE `Users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Task`
--
ALTER TABLE `Task`
  MODIFY `task_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `Users`
--
ALTER TABLE `Users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `Task`
--
ALTER TABLE `Task`
  ADD CONSTRAINT `Task_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `Users` (`user_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
