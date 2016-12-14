# Project Walk - Adam, your macro-economic friend!

# Synopsis

This is an **application** for A-Level macro-economic students that connects to [World Bank Data API](https://datahelpdesk.worldbank.org/knowledgebase/topics/125589-developer-information) to get data necesarry to display in a new design manner a few indicators such as: GDP, BOP, Unemployment, Inflation etc.

# Design

### Front End
There are two designs for the UI to interact with the API - simple and advanced. The simple design is developed based on a search bar interface with auto suggestions for country names & codes, indicators and type of charts.

### Back End
The data coming from the API is cached for each query if it does not exist and it also uses an offline resource in order to provide access to the app without Internet Connection.

#### Simple queries
```
unemployment on a map for United States vs United Kingdom vs Germany vs Russian Federation 2010
```
![alt tag](https://doc-14-c4-docs.googleusercontent.com/docs/securesc/mpif32oojb6v7k3mp9828ia18n50jkec/kt14ssh8jevv1ha4ejaaqmp90jj794eu/1481716800000/05860371902366768687/05860371902366768687/0B75PkoKKan1EOFNyaFZjakJ0ZVk?e=download&nonce=a96t1p9tpuric&user=05860371902366768687&hash=v9c91djgkpi4pge6nlp68khe7oblantd)

```
unemployment on a line chart for United States vs United Kingdom from 2000 to 2014
```
![alt tag](https://dl.dropboxusercontent.com/u/57929798/SEG/Screen%20Shot%202016-12-14%20at%201.23.49%20pm.png)

## More information
### Features
* Pressing on the **toggle button** will change the view accordingly to either simple/advanced mode.
* The **text filed** will give suggestions for
    1. Country names (based on a part of the name/country code i.e. *Unite* (for United States) or US)
    2. Type of charts
    3. Indicators
* Clicking **enter** in the **text field** will show missing arguments for the query or the result.
* Quiz for training on the indicators and macro-economic data.
* Speech recognition exists for simple queries such as **exit the app**. The implementation also includes identification for **switch chart to map/line/bar** but with no effect.
* The speech recognition and the synthesizer can be tested by clicking once the avatar (top icon in the app) and saying **exit** once or twice.

## Tests

Describe and show how to run the tests with code examples.

## Versioning

This is version **1.0.0**.

## Authors
* [Hager Abdo](https://github.com/hagerabdo)
* [Marco Paulo Pacheco Costa](https://github.com/mpcosta)
* Michael Peecock
* Prakash Malam
* [Razvan-Gabriel Geangu](https://github.com/RazvanGeangu)
* Robin Mason
