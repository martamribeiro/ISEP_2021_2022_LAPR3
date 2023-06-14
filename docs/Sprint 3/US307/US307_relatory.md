# RELATORY

# US 307 - Get a warning whenever a cargo manifest is issued with a warehouse whose available capacity is insufficient as the destiny

## 1. Requirements Engineering

### 1.1. User Story Description

As Port manager, I intend to get a warning whenever I issue a cargo manifest destined for a warehouse whose available capacity is insufficient to accommodate the new manifest.

### 1.2. Acceptance Criteria

* Destination warehouse is properly identified.
* Warehouse available capacity is properly computed.
* The warning is triggered when required.

### 1.3. Found out Dependencies

This US has dependencies with US306, since it reuses it for calculating the warehouse's occupancy rate.

### 1.4 Input and Output Data

**Input Data:**

* Typed data:
  * cargo manifest ID
  * warehouse ID

* Selected data:
  * n/a


**Output Data:**

* If the number of containers in the manifest exceed the Warehouse's available capacity or not.


### 1.5. System Sequence Diagram (SSD)

![US_307_SSD](./docs/Sprint 3/US307/US_307_SSD.svg)


### 1.6 Other Relevant Remarks

The present US is held many times during the business. As the Port Manager deals with the cargo manifests, he will need to know which warehouses have enough space for the containers to be added.


## 2. OO Analysis

### 2.1. Relevant Domain Model Excerpt

![US_307_DM](./docs/Sprint 3/US307/US_307_DM.svg)


### 2.2. Other Remarks

n/a



## 3. Design - User Story Realization

## 3.1. Sequence Diagram (SD)

![US_307_SD](./docs/Sprint 3/US307/US_307_SD.svg)


## 3.2. Class Diagram (CD)

![US_307_CD](./docs/Sprint 3/US307/US_307_CD.svg)


# 4. Tests

### CheckIfContainerExceedsWarehouseCapacityController class test:


**Test 1:** Test to check that it's possible to create a truck trip when the unloading cargo manifest doesn't exceed the warehouse max capacity.

**Test 2:** Test to check that it isn't possible to create a truck trip when the unloading cargo manifest exceeds the warehouse max capacity.

**Test 3:** Test to check that it's possible to create a truck trip when the unloading cargo manifest doesn't exist.

**Test 4:** Test to check that it's possible to create a truck trip when the truck doesn't exist.