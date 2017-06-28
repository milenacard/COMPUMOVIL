-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Servidor: localhost
-- Tiempo de generación: 01-04-2017 a las 01:28:27
-- Versión del servidor: 5.5.24-log
-- Versión de PHP: 5.4.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `databaseevent`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `event`
--

CREATE TABLE IF NOT EXISTS `event` (
  `id` int(12) NOT NULL AUTO_INCREMENT,
  `foto` varchar(100) DEFAULT NULL,
  `nombre` varchar(25) NOT NULL,
  `descripcion` varchar(100) NOT NULL,
  `puntuacion` decimal(10,0) NOT NULL,
  `responsable` varchar(25) NOT NULL,
  `fecha` varchar(12) NOT NULL,
  `latitud` double NOT NULL,
  `longitud` double NOT NULL,
  `informacionGeneral` varchar(400) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

--
-- Volcado de datos para la tabla `event`
--

INSERT INTO `event` (`id`, `foto`, `nombre`, `descripcion`, `puntuacion`, `responsable`, `fecha`, `latitud`, `longitud`, `informacionGeneral`) VALUES
(1, 'R.drawable.patinaje_artstico', 'Patinaje Artistico', 'Campeonato Mundial Junior 2017', '3', 'Taiwan', '15/03/2017', 25.04776, 121.53185, 'Este campeonato es el segundo en importancia tras los Juegos Olimpicos de Invierno y el Campeonato mundial de patinaje artístico tanto en tamanho como importancia.'),
(2, 'R.drawable.tirodeportivo', 'Tiro Deportivo', 'Copa del Mundo de Shotgun', '4', 'Mexico', '19/03/2017', 16.86336, -99.8901, 'La ciudad de Acapulco será la sede de la Copa del Mundo de Shotgun 2017 de la ISSF'),
(3, 'R.drawable.australia', 'Formula 1', 'Gran Premio de Australia', '3', 'Alemania', '26/03/2017', -27, 133, 'El Gran Premio de Australia de 2017 será la primera carrera de la temporada 2017 de Fórmula 1, que se disputara en el Circuito de Albert Park, en Melbourne (Australia).'),
(4, 'R.drawable.hockey', 'Patinaje sobre hielo', 'Campeonato Mundial femenino 2017', '5', 'EEUU', '31/03/2017', 38, -97, 'Es la maxima competición internacional entre selecciones nacionales de hockey sobre hielo.'),
(5, 'R.drawable.balonmano', 'Balonmano', 'XXV Campeonato Mundial de Balonmano Femenino', '2', 'Alemania', '01/12/2017', 51, 9, 'Un total de veinticuatro selecciones nacionales de cuatro confederaciones continentales competiran por el titulo de campeón mundial, cuyo actual portador es el equipo de Noruega, vencedor del Mundial de 2015.');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
