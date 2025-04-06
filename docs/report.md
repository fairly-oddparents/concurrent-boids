<div align="center">

# Assignment 1

Giangiulli Chiara (1189567; chiara.giangiulli@studio.unibo.it)  
Shtini Dilaver (1189997; dilaver.shtini@studio.unibo.it)  
Terenzi Mirco (1193420; mirco.terenzi@studio.unibo.it)  
10 Aprile 2025

</div>

## Indice

- [Analisi del problema](#analisi-del-problema)
- [Design e Architettura](#design-e-architettura)
- [Comportamento del sistema](#comportamento-del-sistema)
- [Performance](#performance)
- [Verifica](#verifica)

## Analisi del problema
Il problema affrontato in questo assignment è la simulazione del comportamento collettivo di boids, entità autonome che si muovono in uno spazio bidimensionale seguendo tre regole: separazione, allineamento e coesione, come introdotto da Craig Reynolds nel 1986.

Ogni boid, ad ogni iterazione del ciclo di simulazione, deve:

- Analizzare la posizione e velocità di tutti gli altri boid che lo circondano.
- Calcolare la nuova velocità e aggiornare la propria posizione.

La programmazione concorrente permette di parallelizzare questi aggiornamenti, ma alcuni aspetti richiedono particolare attenzione:

- Le velocità e le posizioni di ogni boid sono calcolate in base a quelle dei suoi vicini, bisogna quindi gestire in modo sicuro l'accesso concorrente ai dati condivisi.
- Le fasi di calcolo e aggiornamento devono essere sincronizzate per evitare letture/scritture inconsistenti.
- Per un corretto calcolo della nuova posizione dei boid, è necessario che sia prima calcolata e aggiornata la velocità.
- L'intefaccia utente deve essere reattiva e sincronizzarsi con l'aggiornamento dei boids.


## Design e Architettura

Il progetto è stato strutturato seguendo il pattern MVC (Model-View-Controller), con una netta separazione tra logica della simulazione, interfaccia utente e controllo dell'esecuzione:

- **Model** (_BoidsModel_): contiene la logica della simulazione dei boid. Gestisce la lista dei boid, aggiorna le loro posizioni e velocità secondo le regole di comportamento (separazione, coesione, allineamento).
  Per ciascuna versione è stata poi sviluppata un’estensione del modello che implementa la logica specifica del parallelismo.
- **View** (_BoidsPanel_, _BoidsView_): componente Swing responsabile della rappresentazione grafica dei boid e dell'interazione con l'utente. Fornisce i controlli richiesti (start, stop, pause, sliders, input numerico).

- **Controller** (_BoidsController_): gestisce il ciclo di vita della simulazione e funge da ponte tra la vista e il modello. In base alla versione selezionata, avvia il motore di simulazione corretto.

Questo approccio ha permesso una gestione modulare e flessibile delle diverse versioni concorrenti della simulazione.

### Versione Multithread
In questa versione, ogni boid è stato inizialmente associato ad un thread dedicato. Tuttavia, questa soluzione è stata ritenuta troppo onerosa, in quanto limitata al numero di core della CPU utilizzata, e si è passati ad una suddivisione del gruppo di boids nei thread possibili.

L'implementazione di una _Barriera_ ha permesso di gestire la sincronizzazione tra l'aggiornamento delle velocità e delle posizioni: permette di attendere che tutti i thread abbiano terminato di aggiornare le velocità prima di passare all'aggiornamento delle posizioni.
Per evitare, inoltre, un accesso concorrente alle risorse è stata suddivisa la lettura e il calcolo delle velocità dal loro effettivo aggiornamento, questo per tutti i boid, grazie all'utilizzo di una barriera, per garantire modifiche consistenti ed evitare race conditions.
La view viene aggiornata di conseguenza una volta ultimata la modifica delle posizioni da parte di tutti i thread.

Attraverso l'implementazione di un _Monitor_, che richiede l'acquisizione di un lock per la modifica e la lettura di un valore (_ReentrantLock_ e _Condition_), è stata garantita la mutua esclusione nell'aggiornamento del peso delle tre regole quando l'utente interagisce con gli sliders.
Il _Monitor_ è stato utilizzato anche per la gestione del cambio di stato (_PAUSE_ / _RESUME_ / _RUNNING_) quando il sistema viene messo in pausa o stoppato.

### Versione Task-based
Questa versione sfrutta un thread pool fisso (di dimensione pari al numero di core del sistema più uno), creato tramite _ExecutorService_. 
All'inizio di ogni simulazione, viene creato un insieme di task paralleli (uno per ciascun boid).
Un gruppo di task ha il compito di aggiornare le velocità, mentre l'altro ha il compito di aggiornare le posizioni.
La sincronizzazione è stata gestita sfruttando il framework _Executor_ che permette di ottenere i risultati dei task come _Future_. 
Questo permette di attendere il completamento del lavoro dei task prima di procedere.

Anche in questo caso viene utilizzato il _Monitor_ implementato per la gestione dello stato della simulazione.

### Versione Virtual Thread
//TODO

## Comportamento del sistema
A description of the behaviour of the system using one or multiple Petri Nets, choosing the proper level of abstraction.

## Performance
Performance tests to analyze and discuss the performance of the programs (for each version) compared to the sequential version.
Risultati con 16 Core:
Condizioni Separation - Alignment - Coehsion impostati ad 1

_SpeedUp_
Sequenziale(42827ms)	100 boid - 1000 iteration
Sequenziale(41900ms)	1000 boid - 1000 iteration
Sequenziale(520614ms)	5000 boid - 1000 iteration

	Multithread(19304ms)	100 boid - 1000 iteration: (sequenziale/multithread)	->42827/19304 = 2,218
	Multithread(18933ms)	1000 boid - 1000 iteration: (sequenziale/multithread) 	->41900/18933 = 2,213
	Multithread(76706ms)	5000 boid - 1000 iteration: (sequenziale/multithread) 	->520614/76706= 6,787

	TaskBased(19417ms)	100 boid - 1000 iteration: (sequenziale/taskBased)	->42827/19417 = 2,205
	TaskBased(21634ms)	1000 boid - 1000 iteration: (sequenziale/taskbased) 	->41900/21634 = 1,936
	TaskBased(37369ms)	5000 boid - 1000 iteration: (sequenziale/taskbased) 	->520614/37369= 13,931

	VirtualThread(19473ms)	100 boid - 1000 iteration: (sequenziale/virtualthread)	->42827/19473 = 2,199
	VirtualThread(23141ms)	1000 boid - 1000 iteration: (sequenziale/virtualthread) ->41900/23141 = 1,810
	VirtualThread(66864ms)	5000 boid - 1000 iteration: (sequenziale/virtualthread)	->520614/66864= 7,786

_Efficiency_
E = S/N -> (S = speedup, N = number of processors(16))

	Con 100 boids:
		Multithread	: E -> 0,138
		TaskBased	: E -> 0,137
		VirtualThread	: E -> 0,137

	Con 1000 boids:
		Multithread	: E -> 0,138
		TaskBased	: E -> 0,121
		VirtualThread	: E -> 0,113

	Con 5000 boids:
		Multithread	: E -> 0,424
		TaskBased	: E -> 0,870
		VirtualThread	: E -> 0,486

Ideal efficiency is 1 = all processors are used at full capacity

## Verifica
Verification of the program (a model of it) using JPF. For this point, only the Java multithreaded programming version may be considered.
<pre>
<b>./gradlew runAssignment01Verify</b>

<b>> Task :runAssignment01Verify</b>
[WARNING] unknown classpath element: /Users/mirco/Developer/jpf-template-project/jpf-runner/build/examples
JavaPathfinder core system v8.0 (rev 81bca21abc14f6f560610b2aed65832fbc543994) - (C) 2005-2014 United States Government. All rights reserved.


====================================================== system under test
pcd.ass01.multithreaded.Simulation.main()

====================================================== search started: 04/04/25, 13:52
[WARNING] orphan NativePeer method: jdk.internal.reflect.Reflection.getCallerClass(I)Ljava/lang/Class;

====================================================== results
no errors detected

====================================================== statistics
elapsed time:       00:03:29
states:             new=1344566,visited=2395785,backtracked=3740351,end=288
search:             maxDepth=198,constraints=0
choice generators:  thread=1344566 (signal=0,lock=5378,sharedRef=1238321,threadApi=2,reschedule=8944), data=0
heap:               new=980953,released=778818,maxLive=788,gcCycles=3596188
instructions:       126859809
max memory:         504MB
loaded code:        classes=134,methods=3189

====================================================== search finished: 04/04/25, 13:55

Deprecated Gradle features were used in this build, making it incompatible with Gradle 8.0.

You can use '--warning-mode all' to show the individual deprecation warnings and determine if they come from your own scripts or plugins.

See <a href="https://docs.gradle.org/7.4/userguide/command_line_interface.html#sec:command_line_warnings">https://docs.gradle.org/7.4/userguide/command_line_interface.html#sec:command_line_warnings</a>

<span style="color:green">BUILD SUCCESSFUL</span> in 3m 31s
2 actionable tasks: 2 executed
</pre>
