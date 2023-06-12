import 'dart:io';
import 'package:flutter/services.dart';
import 'package:pspdfkit_flutter/widgets/pspdfkit_widget.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:pspdfkit_example/utils/platform_utils.dart';

class PdfWidget extends StatefulWidget {
  final File extractedDocument;

  const PdfWidget({Key? key, required this.extractedDocument})
      : super(key: key);

  @override
  _PdfWidgetState createState() => _PdfWidgetState();
}

class _PdfWidgetState extends State<PdfWidget> {
  @override
  void initState() {
    super.initState();
    _listenToPdfViewTapped();
  }

  void _listenToPdfViewTapped() {
    const MethodChannel('com.pspdfkit.global')
        .setMethodCallHandler((call) async {
      // print(call.method);

      switch (call.method) {
        case 'spreadIndexDidChange':
          final dynamic pages = call.arguments;
          print(pages);
          break;
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        extendBodyBehindAppBar: PlatformUtils.isAndroid(),
        resizeToAvoidBottomInset: PlatformUtils.isIOS(),
        appBar: AppBar(),
        body: SafeArea(
            top: false,
            bottom: false,
            child: Container(
              padding: PlatformUtils.isCupertino(context)
                  ? null
                  : const EdgeInsets.only(top: kToolbarHeight),
              child:
                  PspdfkitWidget(documentPath: widget.extractedDocument.path),
            )));
    // GestureDetector(
    //   onTapDown: (details) {
    //     print(details.globalPosition);
    //     print(details.localPosition);
    //   },
    //   child: PspdfkitWidget(
    //       documentPath: widget.extractedDocument.path),
    // ))));
  }
}
