Reveal.getSlides().forEach(function(s){
  s.querySelectorAll("object").forEach(function(e) {
    if (e.contentDocument)
      e.parentElement.replaceChild(e.contentDocument.documentElement.cloneNode(true), e);
    });
  });
