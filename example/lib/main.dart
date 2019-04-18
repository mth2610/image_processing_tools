import 'dart:io';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:image_processing_tools/image_processing_tools.dart';
import 'package:image_picker/image_picker.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  File _image;
  File _proceessedImage;
  String _dcimPath;
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
            _dcimPath != null
            ? Container(
              padding: const EdgeInsets.all(16),
              child: Text("DCIM path: $_dcimPath")
            )
            : Container(),
            _buildImagePickerButton(),
            _buildConvertToGrey(),
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
            image: FileImage(_proceessedImage),
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
              });
            },
          ),
          IconButton(
            icon: Icon(Icons.folder),
            onPressed: ()async{
              var image = await ImagePicker.pickImage(source: ImageSource.gallery);
              setState(() {
                _image = image;
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
            _proceessedImage = await ImageProcessingTools.convertToGray(_image.path);
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
