import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Pixmap Example',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: Scaffold (
        appBar: AppBar(
          title: Text(
            "Flutter Pixmap"
          ),
        ),
        body: PixmapSearcher()
      )
    );
  }
}

class PixmapSearcher extends StatefulWidget {
  @override
  _PixmapSearcherState createState() => _PixmapSearcherState();
}

class _PixmapSearcherState extends State<PixmapSearcher> {
  final url = 'https://pixabay.com/api/?q={0}&key=8831692-b1c2e4ac049f22756c0be1b6c&image_type=photo';
  var searchFieldController = TextEditingController();
  var _loading = false;
  var _gridWievItemsPerRow = 2;
  List<Widget> images = List<Widget>();

  @override
  void dispose() {
    searchFieldController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Column(
      mainAxisSize: MainAxisSize.max,
      children: <Widget>[
        _getSearchField(),
        Expanded(
          child: _getImageView()
        )
      ],
    );
  }

  Widget _getSearchField() {
    return Row(
      children: <Widget>[
        Expanded(
          flex: 3,
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 20.0),
            child: TextField(
              enabled: !_loading,
              controller: searchFieldController,
            )
          ),
        ),
        Expanded (
          flex: 1,
          child: Padding(
            padding: const EdgeInsets.only(right: 20.0),
            child: RaisedButton(
              child: Text("Search"),
              onPressed: _loading ? null : _searchButtonPressed,
            )
          ),
        )
      ],
    );
  }

  void _searchButtonPressed() {
    setState(() {
      _loading = true;
    });
    _sendRequest();
  }

  Widget _getImageView() {
    if(_loading) {
      return Center(
        child: CircularProgressIndicator()
      );
    } else {
      return GridView.count(
        crossAxisCount: _gridWievItemsPerRow,
        children: images
      );
    }
  }

  void _sendRequest() async  {
    final searchQ = searchFieldController.text;
    final completedUrl = url.replaceFirst('{0}', searchQ);
    try {
      final response = await http.get(completedUrl);
      if (response.statusCode == 200) {
        _gridWievItemsPerRow = 2;
        final extractedData = jsonDecode(response.body);
        List loadedCars = extractedData['hits'];
        images.clear();
        for (var i in loadedCars) {
          images.add(
              Image.network(
                i["previewURL"],
                fit: BoxFit.cover
              )
          );
        }
      } else {
        throw("error network");
      }
    } catch (error) {
      _gridWievItemsPerRow = 1;
      images.clear();
      images.add(
        Image(
            image: AssetImage('assets/no_image.png')
        )
      );
    }
    setState(() {
      _loading = false;
    });
  }
}
