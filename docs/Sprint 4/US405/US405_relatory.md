# RELATORY

# US 405 - Know the average occupancy rate per manifest of a given ship during a given period.

## 1. Requirements Engineering

### 1.1. User Story Description

As Fleet Manager, I want to know the average occupancy rate per manifest of a given ship during a given period.

### 1.2. Acceptance Criteria

* Ship is properly identified and considered.
* Period is properly identified and considered.
* Average occupancy rate per manifest and ship is correctly computed.

### 1.3. Found out Dependencies

This US has no big dependencies with other USs.

### 1.4 Input and Output Data

**Input Data:**

* Typed data:
    * ship
    * beginning date
    * ending date

* Selected data:
    * n/a


**Output Data:**

* Average occupancy rate of a given ship during a given period.


### 1.5. System Sequence Diagram (SSD)

![US_405_SSD](./docs/Sprint 4/US405/US_405_SSD.svg)


### 1.6 Other Relevant Remarks

The present US is held many times during the business. As the Fleet Manager manages the ships, he will need to know how is (was) its occupancy rate (at a certain time).


## 2. OO Analysis

### 2.1. Relevant Domain Model Excerpt

![US_405_DM](./docs/Sprint 4/US405/US_405_DM.svg)


### 2.2. Other Remarks

n/a



## 3. Design - User Story Realization

## 3.1. Sequence Diagram (SD)

![US_405_SD](./docs/Sprint 4/US405/US_405_SD.svg)


## 3.2. Class Diagram (CD)

![US_405_CD](./docs/Sprint 4/US405/US_405_CD.svg)


# 4. Tests

### AverageOccupancyShipTimeController class test:


**Test 1:** Test to check that the average in correctly calculated for valid values.

**Test 2:** Test to check that the average is -1 (invalid) when shipID doesn't exist.

**Test 3:** Test to check that the average is -1 (invalid) when month is minor than 1.

**Test 4:** Test to check that the average is -1 (invalid) when month is bigger than 12.

**Test 5:** Test to check that the average is -1 (invalid) when 31 days month has more than 31 days.

**Test 6:** Test to check that the average is -1 (invalid) when 30 days month has more than 30 days.

**Test 7:** Test to check that the average is -1 (invalid) when 29 days month has more than 29 days.

**Test 8:** Test to check that the average is -1 (invalid) when 28 days month has more than 28 days.

**Test 9:** Test to check that the average is -1 (invalid) when ending date comes after current date.

**Test 10:** Test to check that the average is -1 (invalid) when beginning date comes after ending date.