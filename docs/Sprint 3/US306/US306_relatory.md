# RELATORY

# US 306 - Know the occupancy rate for each warehouse and an estimate of the containers leaving the warehouse during the next 30 days.

## 1. Requirements Engineering

### 1.1. User Story Description

As a Port manager, I want to know the occupancy rate for each warehouse and an estimate of the containers leaving the warehouse during the next 30 days.

### 1.2. Acceptance Criteria

* For each warehouse the required output is available.
* The 30 days period is properly considered.

### 1.3. Found out Dependencies

This US has no dependencies with other USs, since this is the first US related to the warehouses.

### 1.4 Input and Output Data

**Input Data:**

* Typed data:
    * warehouse id

* Selected data:
    * n/a


**Output Data:**

* Warehouse's occupancy rate and an estimate of the containers leaving the warehouse during the next 30 days.


### 1.5. System Sequence Diagram (SSD)

![US_306_SSD](./docs/Sprint 3/US306/US_306_SSD.svg)


### 1.6 Other Relevant Remarks

The present US is held many times during the business. As the Port Manager manages the containers, he will need to know which warehouses have enough space for them.


## 2. OO Analysis

### 2.1. Relevant Domain Model Excerpt

![US_306_DM](./docs/Sprint 3/US306/US_306_DM.svg)


### 2.2. Other Remarks

n/a



## 3. Design - User Story Realization

## 3.1. Sequence Diagram (SD)

![US_306_SD](./docs/Sprint 3/US306/US_306_SD.svg)


## 3.2. Class Diagram (CD)

![US_306_CD](./docs/Sprint 3/US306/US_306_CD.svg)


# 4. Tests

### CheckOccupancyRatesAndEstimationsWarehouseController class test:


**Test 1:** First test to check that the occupancy rate is correctly calculated for valid values.

**Test 2:** Second test to check that the occupancy rate is correctly calculated for valid values.

**Test 3:** Test to check that the occupancy rate is -1 (invalid) when the warehouse does not exist.

**Test 4:** Test to check that the number of containers leaving the warehouse in the next 30 days is correctly calculated for valid values.

**Test 5:** Test to check that the number of containers leaving the warehouse in the next 30 days is -1 (invalid) when the warehouse does not exist.