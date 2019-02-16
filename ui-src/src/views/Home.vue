<template>
  <div class="home">
    <StaMa msg="Current State of App"/>
  </div>
</template>

<script>
// @ is an alias to /src
import StaMa from '@/components/StaMa.vue'

export default {
  name: 'home',
  components: {
    StaMa
  }
}
</script>
