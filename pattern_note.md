# Pattern

pattern.org
autor: Jiri Palas
email: palas.jiri@gmail.com

Pattern is tool for semi-atomatic exploration and evaluation of microscopic images. It has been originaly developed as master thesis with aim for images from TEM (Transmission Electron Microscopy). It's written in Netbeans Platform 8.0.2

## Core Modules
Project has modular structure and contains following modules:

###### Pattern API  
- package: org.pattern.api
- folder: pattern.api    
- contains basic api for pattern application
  
###### Pattern Core  
- package: org.pattern.core
- folder: pattern.core  
- contains basic datastructures used through application

###### Pattern Hierarchical Cluster Analysis
- package: org.pattern.analysis.cluster.hierarch
- folder: PatternClusterAnalysis
- contains functionality for cluster analysis with hierarchical clustering

###### Pattern UI Utilities
-package: org.pattern.ui.utils
-contains shareable gui elements used in application

###### Pattern Contour Detection
- package: org.pattern.detection.contour
- contains detection algorithm for detecting particles as contours

###### Pattern Image Viewer
- package: org.pattern.image.viewer
- contains image viewer which is capable of displaying image and particles

###### Pattern Image Editor
- package: org.pattern.image.editor
- provides functionality for viewing and editing image with particles

###### Pattern Image Info
- package: org.pattern.image.info
- provides basic info about selected image

###### Pattern Image Inspector/Explorer
- package: org.pattern.image.inspector
- provides info about selected particles, how many, how many of each category

###### Pattern Classificator UI
- package: org.pattern.classification.ui
- provides ui controls for interacting with classificator
- TODO: New classifciator wizzard
		- provides options to choose classification algorithm
		
###### Classification Algorithm KNN
- package: org.pattern.classification.algorithm.knn
- provides KNN classificator which clasifies the data
- Note: service provider ClassificationAlgorithm


###### Image Loaders:
Pattern Image Loader JPEG
Pattern Image Loader TIFF
Pattern Image Loader PNG
Pattern Image Loader MRC


## Used libraries:
- Wrapper OpenCV
- Wrapper Gson
- Wrapper JapuraGUI
- Wrapper BioFormats
- Wrapper HierarchicalClustering

## Notes
- add project menu
	- > new classificator
		- if classificator already exists, add option to create new one
		- > have user to choose from classification algorithms available
	- > save clasificator
	- > close project
	- > open project

- image menu
	+ save image

