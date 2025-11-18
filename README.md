This project is an implementation of a calculator that manages computation of dimensional quantities. The intention is ultimately for a Desmos-style interface, that allows for the assignment of variables and real-time updates to all corresponding equations as they are modified.

A (somewhat informal) release targeted to Windows is available for download on DropBox [here](https://www.dropbox.com/scl/fi/etyn8eet4z4xoueveet90/Physics-Calculator.zip?rlkey=ruwdq2lfpovkve8cdlsu1cidd&st=n90sq16u&dl=0), and should be executable without any installation. It is also preloaded with a small sample file demonstrating some of the features described below.

Current feaures:
- Standard SI units and prefixes
- Selecton of non-standard units:
    - `eV`: electron volt
    - `min`: minute
    - `h`: hour
    - `au`: astronomical unit
    - `Da`: dalton
    - `pc`: parsec
    - `bar`: bar
    - `atm`: atmosphere
    - `cal`: food calorie
- Basic operations and trigonometric functions
- Real-time LaTeX rendering
- Save/load current equations to file
- Variable assignment
- Variable update propogation

Planned features:
- (Possible) rewrite in Dart/Flutter for improved interface
- Equation groups to localize scope of variables
- More comprehensive available functions
- Move hardcoded constants into JSON for better flexibility
- Special molar mass function to calculate molar mass of arbitrary molecular formulae
- LaTeX style interpretation of Greek characters (e.g., \gamma, \mu)
- Allow subscripting of variables
- Some level of symbolic computation (so that, for instance, $\mathrm{sin} (\pi)=0$ instead of $\mathrm{sin}(\pi) \approx 0$).

Notes for LaTeX rendering:
- Units are unitalicized, while variables are italicized
- A space is added between units to disambiguate situations where units / variables overlap with SI prefixes (e.g., $\mathrm{mm}$ may be $\mathrm{millimeter}$ or $\mathrm{meter}^2$)

Special Functions:
- con(): Gets a predefined constant (e.g., $\mathrm{con}(c) = 3.00*10^8m/s$.)
- M(): Gets the mass of the given nucleus (e.g., $\mathrm{M}(12C)$)
- BE(): Gets the binding energy of the given nucleus (e.g., $\mathrm{BE}(12C)$)


Defined constants:
 - `pi`: pi
 - `e`: Euler's number
 - `c`: speed of light in vacuum
 - `h`: Planck's constant
 - `h-`: Reduced Planck's constant
 - `e0`: electric constant
 - `kb`: Boltzmann constant
 - `G`: gravitational constant
 - `ke`: Coulomb's constant
 - `sigma`: Stefan-Boltzmann constant
 - `e`: elementary charge
 - `alpha`: fine-structure constant
 - `uB`: Bohr magneton
 - `uN`: nuclear magneton
 - `a0`: Bohr radius
 - `R`: Rydberg constant
 - `NA`: Avogadro's number
 - `mp`: proton mass
 - `mn`: neutron mass
 - `me`: electron mass