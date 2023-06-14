# Data Dictionary

>**A Data Dictionary is a collection of names, definitions, and attributes about data elements that are being used or captured in a database, information system, or part of a research project.**


##Country
| **_Field Name_**  | **_Data Type_** | **_Data Format_** | **_Field Size_** | **_Description_** | **_Example_** |
|:------------------|:----------------|:------------------|:-----------------|:------------------|:--------------|
| **country_name** | varchar | n/a | 70  | Name for Country | Cyprus |
| **continent** | varchar | n/a | 30 | Continent for Country | Europe |

###Restrictions

**country_name:** Vachar field as primary key, cannot be null.

**continent:** Varchar field, cannot be null.


##Ship
| **_Field Name_**  | **_Data Type_** | **_Data Format_** | **_Field Size_** | **_Description_** | **_Example_** |
|:------------------|:----------------|:------------------|:-----------------|:------------------|:--------------|
| **mmsi**  | integer | NNNNNNNNN | 9 | Unique MMSI Code for all ships | 211331640 |
| **imo**  | varchar | IMONNNNNNN | 10 | Unique IMO Code for all ships | IMO9193305 |
| **calSign**  | varchar | n/a | 4 | Unique Call Sign for all ships | DHBN |
| **shipName**  | varchar | n/a | 30 | Name for Ship | SEOUL EXPRESS |
| **vesselTypeId**  | integer | n/a |  | ID for Ship's Vessel Type | 70 |
| **currentCapacity**  | numeric | n/a |  | Ship's Cargo | 79 |
| **draft**  | numeric | n/a |  | Ship's Draft | 13.6 |

###Restrictions

**mmsi:** Integer field as primary key, cannot be null.

**imo:** Unique varchar field, cannot be null.

**callSign:** Unique varchar field, cannot be null.

**shipName:** Varchar field, cannot be null.

**vesselTypeId:** Integer field as a foreign key (from Vessel Type), cannot be null.

**currentCapacity:** Numeric field with 2 decimal places and at max 6 digits(counting decimals).

**draft:** Numeric field with 2 decimal places and at max 5 digits(counting decimals), cannot be null and needs to be bigger than 0.


##Vessel Type
| **_Field Name_**  | **_Data Type_** | **_Data Format_** | **_Field Size_** | **_Description_** | **_Example_** |
|:------------------|:----------------|:------------------|:-----------------|:------------------|:--------------|
| **length**  | numeric | n/a | 6 | Ship's Length | 294 |
| **width**  | numeric | n/a | 6 | Ship's Width | 32 |
| **maxCapacity** | numeric | n/a | 6 | Ship's max capacity | 79

###Restrictions

**length:** Numeric field with 2 decimal places and at max 5 digits(counting decimals), not nullable.

**width:** Numeric field with 2 decimal places and at max 5 digits(counting decimals), not nullable.


##Place Location
| **_Field Name_**  | **_Data Type_** | **_Data Format_** | **_Field Size_** | **_Description_** | **_Example_** |
|:------------------|:----------------|:------------------|:-----------------|:------------------|:--------------|
| **latitude**  | numeric | n/a | 7 | Latitude for Place's Location | 36.39094 |
| **longitude**  | numeric | n/a | 7 | Longitude for Place's Location | -122.71335 |
| **country** | varchar | n/a | 70 | Country for Place's Location | Cyprus |

###Restrictions

**latitude:** Is a numeric field with 4 decimal places and at max 6 digits(counting decimals), in the range of [-90,91] or 91 (unavailable), cannot be null.

**longitude:** Is a numeric field with 4 decimal places and at max 7 digits(counting decimals), in the range of [-180,180] or 181 (unavailable), cannot be null.

**country:** Vaechar field, not nullable and it refers to the country_name field in the country table.


##Port
| **_Field Name_**  | **_Data Type_** | **_Data Format_** | **_Field Size_** | **_Description_** | **_Example_** |
|:------------------|:----------------|:------------------|:-----------------|:------------------|:--------------|
| **port_id**  | integer | n/a |  | ID for Port | 10136 |
| **name** | varchar | n/a | 40 | Name for Port | Larnaca |
| **latitude**  | numeric | n/a | 7 | Latitude for Ship's Position | 36.39094 |
| **longitude**  | numeric | n/a | 8 | Longitude for Ship's Position | -122.71335 |

###Restrictions

**port_id:** Integer field as primary key of the table.

**name:** Varchar field, cannot be null.

**latitude:** Numeric field as foreign key (from Place Location), with 4 decimal places and at max 6 digits(counting decimals), in the range of [-90,91] or 91 (unavailable), not null.

**longitude:** Numeric field as foreign key (from Place Location), with 4 decimal places and at max 7 digits(counting decimals), in the range of [-180,180] or 181 (unavailable), not null.


##Cargo Manifest
| **_Field Name_**  | **_Data Type_** | **_Data Format_** | **_Field Size_** | **_Description_** | **_Example_** |
|:------------------|:----------------|:------------------|:-----------------|:------------------|:--------------|
| **cargoManifest_id**  | integer | n/a |  | ID for Cargo Manifest |  |
| **port_id**  | integer | n/a |  | ID for Port | 10136 |
| **mmsi**  | integer | NNNNNNNNN | 9 | Unique MMSI Code for Cargo Manifest's Ship | 211331640 |
| **typeOfTransport** | varchar | n/a | 10 | Type of Transport for Cargo Manifest | Ship |
| **status**  | varchar | n/a | 12 | Status for Cargo Manifest | loading |

###Restrictions

**cargoManifest_id:** Integer field as primary key.

**port_id:** Integer field as foreign key (from Port), cannot be null.

**mmsi:** Integer field as foreign key (from Ship), cannot be null.

**typeOfTransport:** Varchar field, cannot be null.

**status:** Varchar field, cannot be null and takes value 'loading' or 'unloading'.


##Ship Position
| **_Field Name_**  | **_Data Type_** | **_Data Format_** | **_Field Size_** | **_Description_** | **_Example_** |
|:------------------|:----------------|:------------------|:-----------------|:------------------|:--------------|
| **baseDateTime**  | date | DD/MM/YYYY HH:MM | 16 | Date for Ship's Position | 31/12/2020 01:25 |
| **latitude**  | numeric | n/a | 7 | Latitude for Ship's Position | 36.39094 |
| **longitude**  | numeric | n/a | 7 | Longitude for Ship's Position | -122.71335 |
| **mmsi**  | integer | NNNNNNNNN | 9 | Unique MMSI Code for the Ship related to the Ship Position | 211331640 |
| **sog**  | numeric | n/a | 6 | SOG for Ship's Position | 19.7 |
| **cog**  | numeric | n/a | 6 | COG for Ship's Position | 145.5 |
| **heading**  | numeric | n/a | 6 | Heading for Ship's Position | 147 |
| **transceiver**  | varchar | n/a | 15 | Transceiver Class for Ship's Position | B |

###Restrictions

**baseDateTime:** Date field as primary key(together with latitude and longitude), cannot be null.

**latitude:** Numeric field as primary key(together with baseDateTime and longitude), with 4 decimal places in the range of [-90,91] or 91 (unavailable) and at max 6 digits(counting decimals).

**longitude:** Numeric fieldas primary key(together with latitude and baseDateTime), with 4 decimal places in the range of [-180,180] or 181 (unavailable) and at max 7 digits(counting decimals).

**mmsi:** Integer field as foreign key (from Ship), cannot be null.

**sog:** Numeric field, cannot be null, with 2 decimal places and at max 5 digits(counting decimals).

**cog:** Numeric field, cannot be null, with 2 decimal places and at max 5 digits(counting decimals), in the range of [0,359].

**heading:** Numeric field, cannot be null, with 2 decimal places and at max 5 digits(counting decimals), in the range of [0,359] or 511 (unavailable).

**transceiver:** Varchar field, cannot be null.


##EnergyPower
| **_Field Name_**  | **_Data Type_** | **_Data Format_** | **_Field Size_** | **_Description_** | **_Example_** |
|:------------------|:----------------|:------------------|:-----------------|:------------------|:--------------|
| **numEnergyGenerator**  | integer | n/a |  | Number of Energy Generator for Energy Power | 4 |
| **mmsi**  | integer | NNNNNNNNN | 9 | Unique MMSI Code for the Ship related to the Energy Power | 211331640 |
| **generatorPower** | numeric | n/a | 6 | Generators Power for Energy Power |  |

###Restrictions

**numEnergyGenerator:**  Integer field as primary key (together with mmsi).

**mmsi:** Integer field as foreign key (from Ship) and primary key (together with numEnergyGenerator), cannot be null.

**generatorPower:** Numeric field with 2 decimal places and at max 5 digits(counting decimals).


##Container
| **_Field Name_**  | **_Data Type_** | **_Data Format_** | **_Field Size_** | **_Description_** | **_Example_** |
|:------------------|:----------------|:------------------|:-----------------|:------------------|:--------------|
| **container_id** | integer | n/a |  | ID for container |  |
| **payload** | numeric | n/a | 6 | Payload for container |  |
| **tare** | numeric | n/a | 6 | Tare for container |  |
| **gross** | numeric | n/a | 6 | Gross for container |  |
| **isoCode** | varchar | NNNN | 4 | ISO Code for container |  |

###Restrictions

**container_id:** Integer field as primary key, cannot be null.

**payload:** Numeric field with 2 decimal places and at max 5 digits(counting decimals), cannot be null and less than 0.

**tare:** Numeric field with 2 decimal places and at max 5 digits(counting decimals), cannot be null and less than 0.

**gross:** Numeric field with 2 decimal places and at max 5 digits(counting decimals), cannot be null and less than 0.

**isoCode:** Varchar field, cannot be null.


##Refrigeration
| **_Field Name_**  | **_Data Type_** | **_Data Format_** | **_Field Size_** | **_Description_** | **_Example_** |
|:------------------|:----------------|:------------------|:-----------------|:------------------|:--------------|
| **container_id** | integer | n/a |  | ID for Refrigerated Container |  |
| **temperatureKept** | numeric | n/a | 6 | Temperature Kept for Refrigerated Container |  |

###Restrictions

**container_id:** Integer field as foreign key (from Container) and primary key (together with temperatureKept), cannot be null.

**temperatureKept:**  Numeric field as primary key (together with container_id), with 2 decimal places and at max 5 digits(counting decimals).


##Position In Vehicule
| **_Field Name_**  | **_Data Type_** | **_Data Format_** | **_Field Size_** | **_Description_** | **_Example_** |
|:------------------|:----------------|:------------------|:-----------------|:------------------|:--------------|
| **cargoManifest_id** | integer | n/a |  | ID for Cargo Manifest related to the Vehicule |  |
| **container_id** | integer | n/a |  | ID for Container related to Position in Vehicule |  |
| **containerPositionX** | integer | n/a |  | X Axis Value for Position in Vehicule | |
| **containerPositionY** | integer | n/a |  | Y Axis Value for Position in Vehicule | |
| **containerPositionZ** | integer | n/a |  | Z Axis Value for Position in Vehicule | |

###Restrictions

**cargoManifest_id:** Integer field as foreign key (from Cargo Manifest), cannot be null.

**container_id:** Integer field as foreign key (from Container), cannot be null.

**containerPositionX:** Integer field as primary key (together with containerPositionY and containerPositionZ), cannot be null.

**containerPositionY:** Integer field as primary key (together with containerPositionX and containerPositionZ), cannot be null.

**containerPositionZ:** Integer field as primary key (together with containerPositionY and containerPositionX), cannot be null.


##Container In Cargo Manifest
| **_Field Name_**  | **_Data Type_** | **_Data Format_** | **_Field Size_** | **_Description_** | **_Example_** |
|:------------------|:----------------|:------------------|:-----------------|:------------------|:--------------|
| **container_id** | integer |  |  | ID for Container in Cargo Manifest |  |
| **cargoManifest_id** | integer |  |  | ID for Cargo Manifest |  |

###Restrictions

**container_id:** Integer field as foreign key (from Cargo Manifest) and primary key (together with cargoManifest_id), cannot be null.

**cargoManifest_id:** Integer field as foreign key (from Container) and primary key (together with container_id), cannot be null.