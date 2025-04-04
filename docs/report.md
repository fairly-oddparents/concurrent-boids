<div align="center">

# Assignment 1

Giangiulli Chiara (1189567; chiara.giangiulli@studio.unibo.it)  
Shtini Dilaver (1189997; dilaver.shtini@studio.unibo.it)  
Terenzi Mirco (1193420; mirco.terenzi@studio.unibo.it)  
10 Aprile 2025

</div>

## Table of Contents

- [Problem Analysis](#problem-analysis)
- [Design and Architecture](#design-and-architecture)
- [System Behaviour](#system-behaviour)
- [Performance](#performance)
- [Verification](#verification)

## Problem Analysis
A brief analysis of the problem, focusing on particular aspects that are relevant from a concurrent point of view.

## Design and Architecture
A description of the adopted design, strategy, and architecture.

## System Behaviour
A description of the behaviour of the system using one or multiple Petri Nets, choosing the proper level of abstraction.

## Performance
Performance tests to analyze and discuss the performance of the programs (for each version) compared to the sequential version.

## Verification
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
