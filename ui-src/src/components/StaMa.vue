<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    <div id="svg-stama" >
    </div>
    <p>{{stamaSVG}}</p>
  </div>
</template>

<script>
  const smcat = require("state-machine-cat");
  export default {
    name: 'StaMa',
    props: {
      msg: String
    },
    computed: {
    },
    data() {
      return {
        stamaSVG: "<b>aaaa</b>"
      }
    },
    mounted() {
      this.axios.get("/api/stama/state")
      .then((resp) => this.stamaSVG = this.render(resp.data));
    },
    methods: {
      render(data) {
        let lSVGInAString = smcat.render(data, {
          inputType: "smcat-json", //unknown value actually leads to pass through
          outputType: "svg",
          direction: "left-right"
        });
        this.stamaSVG = lSVGInAString;
        document.getElementById("svg-stama").innerHTML = lSVGInAString;
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
