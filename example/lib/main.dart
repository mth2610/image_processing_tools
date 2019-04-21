import 'dart:io';

import 'package:flutter/material.dart';
import 'package:path_provider/path_provider.dart';
import 'package:image_processing_tools/image_processing_tools.dart';
import 'package:image_picker/image_picker.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  File _image;
  String _proceessedImage;
  bool _isSaving;
  
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: _isSaving == true
        ? Center(child: CircularProgressIndicator())
        : ListView(
          children: <Widget>[
            _buildPickedImage(),
            _buildImagePickerButton(),
            _buildConvertToGrey(),
            _buildConvertToOilpaintingl(),
            _buildConvertCartoon(),
            _buildConvertToPencilColor(),
            _buildConvertToPencilGrey(),
          ],
        ),
      ),
    );
  }

  Widget _buildPickedImage(){
    return _proceessedImage != null
    ? Container(
      margin: EdgeInsets.all(16),
      height: 300.0,
      decoration: BoxDecoration(
          border: Border.all(width: 1),
          image: DecorationImage(
            image: FileImage(File(_proceessedImage)),
          )
        )
    )
    : Container(
      margin: EdgeInsets.all(16),
      height: 300.0,
      child: _image!=null
        ? null
        : Center(
          child: Container(
            child: Text("No selected image"),
          ),
        ),
      decoration: _image!=null
        ? BoxDecoration(
          border: Border.all(width: 1),
          image: DecorationImage(
            image: FileImage(_image),
          )
        )
        : BoxDecoration(
          border: Border.all(width: 1)
        ),
    );
  }

  Widget _buildImagePickerButton(){
    return Container(
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          IconButton(
            icon: Icon(Icons.camera_alt),
            onPressed: ()async{
              var image = await ImagePicker.pickImage(source: ImageSource.camera);
              setState(() {
                _image = image;
                _proceessedImage = null;
              });
            },
          ),
          IconButton(
            icon: Icon(Icons.folder),
            onPressed: ()async{
              var image = await ImagePicker.pickImage(source: ImageSource.gallery);
              setState(() {
                _image = image;
                _proceessedImage = null;
              });
            },
          )
        ],
      ),
    );
  }

  Widget _buildConvertToGrey(){
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 16.0),
      child: RaisedButton(
        color: Colors.blue,
        textColor: Colors.white,
        child: Text("Convert to grey"),
        onPressed: ()async{
          setState(() {
            _isSaving = true;
          });
          if(_image!=null){
            Directory tempDir = await getTemporaryDirectory();
            String tempPath = tempDir.path; 
            _proceessedImage = await ImageProcessingTools.detectCircle(_image.path, tempPath, 500);
            setState(() {
              _isSaving = false;
            });
          } else {
            setState(() {
              _isSaving = false;
            });
          }
        },
      ),
    );
  }

    Widget _buildConvertCartoon(){
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 16.0),
      child: RaisedButton(
        color: Colors.blue,
        textColor: Colors.white,
        child: Text("Convert to cartoon"),
        onPressed: ()async{
          setState(() {
            _isSaving = true;
          });
          if(_image!=null){
            Directory tempDir = await getTemporaryDirectory();
            String tempPath = tempDir.path; 
            _proceessedImage = await ImageProcessingTools.toCartoon(_image.path, tempPath);
            setState(() {
              _isSaving = false;
            });
          } else {
            setState(() {
              _isSaving = false;
            });
          }
        },
      ),
    );
  }

  Widget _buildConvertToPencilColor(){
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 16.0),
      child: RaisedButton(
        color: Colors.blue,
        textColor: Colors.white,
        child: Text("Convert to color pencil"),
        onPressed: ()async{
          setState(() {
            _isSaving = true;
          });
          if(_image!=null){
            Directory tempDir = await getTemporaryDirectory();
            String tempPath = tempDir.path; 
            _proceessedImage = await ImageProcessingTools.toPencilSketch(_image.path, tempPath, false);
            setState(() {
              _isSaving = false;
            });
          } else {
            setState(() {
              _isSaving = false;
            });
          }
        },
      ),
    );
  }

  Widget _buildConvertToPencilGrey(){
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 16.0),
      child: RaisedButton(
        color: Colors.blue,
        textColor: Colors.white,
        child: Text("Convert to grey pencil"),
        onPressed: ()async{
          setState(() {
            _isSaving = true;
          });
          if(_image!=null){
            Directory tempDir = await getTemporaryDirectory();
            String tempPath = tempDir.path; 
            _proceessedImage = await ImageProcessingTools.toPencilSketch(_image.path, tempPath, true);
            setState(() {
              _isSaving = false;
            });
          } else {
            setState(() {
              _isSaving = false;
            });
          }
        },
      ),
    );
  }

  Widget _buildConvertToOilpaintingl(){
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 16.0),
      child: RaisedButton(
        color: Colors.blue,
        textColor: Colors.white,
        child: Text("Convert to oil paiting"),
        onPressed: ()async{
          setState(() {
            _isSaving = true;
          });
          if(_image!=null){
            Directory tempDir = await getTemporaryDirectory();
            String tempPath = tempDir.path; 
            _proceessedImage = await ImageProcessingTools.toOilPaiting(_image.path, tempPath);
            setState(() {
              _isSaving = false;
            });
          } else {
            setState(() {
              _isSaving = false;
            });
          }
        },
      ),
    );
  }

}
