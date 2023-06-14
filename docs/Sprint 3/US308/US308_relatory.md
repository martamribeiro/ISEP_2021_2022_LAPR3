# RELATORY

# US 308 - Ensure that the number of containers in a manifest does not exceed the ship's available capacity.

## 1. Requirements Engineering

### 1.1. User Story Description

As a Traffic manager, I want to have a system that ensures that the number of containers in a manifest does not exceed the ship's available capacity.

### 1.2. Acceptance Criteria

* The destination ship is properly identified.
* Ship's available capacity is properly computed.
* The warning is triggered when required.

### 1.3. Found out Dependencies

This US has dependencies with US208 and US209, since it reuses them for calculating the ship's occupancy rate.

### 1.4 Input and Output Data

**Input Data:**

* Typed data:
    * ship trip ID
    * mmsi
    * departure location
    * arrival location
    * loading cargo manifest id
    * estimated departure date
    * estimated arrival date

* Selected data:
    * n/a


**Output Data:**

* If the number of containers in the manifest exceed the Ship's available capacity or not.


### 1.5. System Sequence Diagram (SSD)

![US_308_SSD](./docs/Sprint 3/US308/US_308_SSD.svg)


### 1.6 Other Relevant Remarks

The present US is held many times during the business. As the Traffic Manager manages the containers, he will need to know which ships have enough space for them.


## 2. OO Analysis

### 2.1. Relevant Domain Model Excerpt

![US_308_DM](./docs/Sprint 3/US308/US_308_DM.svg)


### 2.2. Other Remarks

n/a



## 3. Design - User Story Realization

## 3.1. Sequence Diagram (SD)

![US_308_SD](./docs/Sprint 3/US308/US_308_SD.svg)


## 3.2. Class Diagram (CD)

![US_308_CD](./docs/Sprint 3/US308/US_308_CD.svg)


# 4. Tests

### ShipOccupancyRatesController class test:


**Test 1:** Test to check if that the ship is created when the desired ship has enough space for the containers in the cargo manifest.

**Test 2:** Test to check if the ship is not created when the cargo manifest ID has too much container for the desired ship.

**Test 3:** Test to check if the ship is not created when the cargo manifest ID is invalid.

**Test 4:** Test to check if the ship is not created when the ship ID (mmsi) is invalid.