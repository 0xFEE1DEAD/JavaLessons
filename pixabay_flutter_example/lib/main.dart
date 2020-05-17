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
  List<String> images = List<String>();

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
        // Create a grid with 2 columns. If you change the scrollDirection to
        // horizontal, this produces 2 rows.
        crossAxisCount: 2,
        // Generate 100 widgets that display their index in the List.
        children: images.map((String url) {
          return Image.network(url, fit: BoxFit.cover);
        }).toList()
      );
    }
  }

  void _sendRequest() async  {
    var searchQ = searchFieldController.text;
    var completedUrl = url.replaceFirst('{0}', searchQ);
    final response = await http.get(completedUrl);
    final extractedData = jsonDecode(response.body);
    List loadedCars = extractedData['hits'];
    images.clear();
    for(var i in loadedCars) {
      images.add(i["previewURL"]);
    }
    setState(() {
      _loading = false;
    });
  }
}
