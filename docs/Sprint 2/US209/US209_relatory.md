# RELATORY

# US 209 - Know the occupancy rate of a given ship at a given moment

## 1. Requirements Engineering

### 1.1. User Story Description

As a Ship Captain, I want to know the occupancy rate of a given ship at a given moment

### 1.2. Acceptance Criteria

* Ship is properly identified.
* Reuses US208.
* Occupancy rate is properly computed.

### 1.3. Found out Dependencies

This US has dependencies with the US101, since the business data base needs to exist in order to analyze it, and US208, since it reuses its methods.

### 1.4 Input and Output Data

**Input Data:**

* Typed data:
    * mmsi
    * date

* Selected data:
    * n/a


**Output Data:**

* occupancy rate of a give ship at a given moment


### 1.5. System Sequence Diagram (SSD)

![US_209_SSD](./docs/US209/US_209_SSD.svg)


### 1.6 Other Relevant Remarks

The present US is held many times during the business. As the Ship Captain manages his ship, he will need to know its occupancy rate many times.


## 2. OO Analysis

### 2.1. Relevant Domain Model Excerpt

![US_209_DM](./docs/US209/US_209_DM.svg)

### 2.2. Other Remarks

n/a



## 3. Design - User Story Realization

## 3.1. Sequence Diagram (SD)

![US_209_SD](./docs/US209/US_209_SD.svg)

## 3.2. Class Diagram (CD)

![US_209_CD](./docs/US209/US_209_CD.svg)

# 4. Tests

### ShipOccupancyRatesController class test:

**Test 1:** Test to check if the occupancy rate is correctly calculates when the values are valid.

In order to test this, it's important to define valid values for maxCapacity, initialNumContainers, addedContainersNum and removedContainersNum.
Then, the occupancy rate should be manually calculated and its result should be compared with the one given by the algorithm.

**Test 2:** Test to check if the occupancy rate is -1 when the values are invalid.

This test is similar with test 1, but should have invalid values (result <0% or >100%).
Then, the returned value given by the algorithm should be -1.

**Test 3:** Test to check the occupancy rate by cargo manifest is correctly calculated when the cargo manifest exists in the Data Base.

**Test 4:** Test to check the occupancy rate by mmsi and date is correctly calculated when the ship with the given mmsi exists in the Data Base.

**Test 5:** Test to check the occupancy rate by cargo manifest throws an exception when the cargo manifest doesn't exist in the Data Base.

**Test 6:** Test to check the occupancy rate by mmsi and date throws an exception when the ship doesn't exist in the Data Base.

**Test 7:** Test to check the occupancy rate by mmsi and date is 0% when the ship has no ship trips in the Data Base as for before the given date.