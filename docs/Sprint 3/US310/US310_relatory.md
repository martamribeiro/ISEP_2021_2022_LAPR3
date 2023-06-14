# RELATORY

# US 310 - Have a map of the occupation of the existing resources in the port during a given month.

## 1. Requirements Engineering

### 1.1. User Story Description

As Port manager, I intend to have a map of the occupation of the existing resources in the port during a given month.

### 1.2. Acceptance Criteria

* Occupation of resources is restricted to the month provided.
* The reported occupation respects actual port capacity.

### 1.3. Found out Dependencies

This US has no big dependencies with other USs.

### 1.4 Input and Output Data

**Input Data:**

* Typed data:
    * port ID
    * month
    * year

* Selected data:
    * n/a


**Output Data:**

* A map of the occupation of the existing resources in the port during a given month.


### 1.5. System Sequence Diagram (SSD)

![US_310_SSD](./docs/Sprint 3/US310/US_310_SSD.svg)


### 1.6 Other Relevant Remarks

The present US is held many times during the business. As the Port Manager manages his port, he will need to know how is its occupacy.


## 2. OO Analysis

### 2.1. Relevant Domain Model Excerpt

![US_310_DM](./docs/Sprint 3/US310/US_310_DM.svg)


### 2.2. Other Remarks

n/a



## 3. Design - User Story Realization

## 3.1. Sequence Diagram (SD)

![US_310_SD](./docs/Sprint 3/US310/US_310_SD.svg)


## 3.2. Class Diagram (CD)

![US_310_CD](./docs/Sprint 3/US310/US_310_CD.svg)


# 4. Tests

### CreateMapContainersPortController class test:


**Test 1:** Test that the given map is correctly made.

**Test 2:** Test that the map is not made for an invalid port id.

**Test 3:** Test that the map is not made for an invalid date (month<1).

**Test 4:** Test that the map is not made for an invalid date (month>12).

**Test 5:** Test that the map is not made for months that have not ended yet.